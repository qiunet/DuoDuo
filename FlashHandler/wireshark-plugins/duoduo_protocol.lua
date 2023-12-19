do
	--[[
		duoduo 的游戏TCP协议
	]]
	local serverTcpPort = 0
	local serverUdpPort = 0

	function createGameProtocol(protoName, protoDesc)
		local p_gameTcp = Proto(protoName, protoDesc)

		local f_magic = ProtoField.bytes(protoName .. '.magic', '魔数')
		local f_length = ProtoField.uint32(protoName .. ".length", "长度", base.DEC)
		local f_proto_id = ProtoField.uint32(protoName .. ".proto_id", "协议Id", base.DEC)
		local f_crc = ProtoField.uint32(protoName .. ".crc", "CRC", base.DEC)

		p_gameTcp.fields = {
			f_magic,
			f_length,
			f_proto_id,
			f_crc
		}

		local data_dis = Dissector.get('data')
		p_gameTcp.dissector = function(buf, pkt, root)
			if pkt.src_port == serverTcpPort or pkt.src_port == serverUdpPort then
				local buf_len = buf:len()
				if buf_len < 8 then
					return false
				end
				local p_desc = pkt.src_port == serverTcpPort and "TCP" or "KCP"
				local subTree = root:add(p_gameTcp, buf)
				pkt.cols.protocol = p_desc .. "_RSP"
				subTree:add(f_length, buf(0, 4))
				subTree:add(f_proto_id, buf(4, 4))

				subTree:append_text(" 协议ID:"..buf(4, 4):uint())
				subTree:append_text(" 长度:"..buf(0, 4):uint())
				data_dis:call(buf(8):tvb(), pkt, root)
				return true
			end
			if pkt.dst_port == serverTcpPort or pkt.dst_port == serverUdpPort then
				local buf_len = buf:len()
				if buf_len < 16 then
					return false
				end

				magic = buf(0, 4)
				if (magic(0,1):uint() ~= 102) or (magic(1,1):uint() ~= 97) or (magic(2,1):uint() ~= 115) or (magic(3,1):uint() ~= 116) then
					print("magic error!")
					return false
				end
				local p_desc = pkt.dst_port == serverTcpPort and "TCP" or "KCP"
				local subTree = root:add(p_gameTcp, buf)
				pkt.cols.protocol = p_desc .. "_REQ"
				subTree:add(f_magic, magic)
				subTree:add(f_length, buf(4, 4))
				subTree:add(f_proto_id, buf(8, 4))
				subTree:add(f_crc, buf(12, 4))

				subTree:append_text(" 协议ID:"..buf(8, 4):uint())
				subTree:append_text(" 长度:"..buf(4, 4):uint())
				data_dis:call(buf(16):tvb(), pkt, root)
				return true
			end
		end
		return p_gameTcp
	end

	function createKcpProtocolDissector(NAME, protoDesc)
		local function CMD_TO_STRING(CMD)
			if CMD:le_uint() == 81 then
				return "PUSH(81)"
			elseif CMD:le_uint() == 82 then
				return "ACK(82)"
			elseif CMD:le_uint() == 83 then
				return "WASK(83)"
			elseif CMD:le_uint() == 84 then
				return "WINS(84)"
			end
			return CMD:le_uint()
		end


		local KCP_HEADER_LENGTH = 24
		local KCP = Proto(NAME, protoDesc)
		-- KCP Protocol Fields.
		local conv  = ProtoField.uint32(NAME .. ".conv", "Conv", base.DEC)
		local cmd   = ProtoField.uint8(NAME .. ".cmd", "Cmd", base.DEC)
		local frg   = ProtoField.uint8(NAME .. ".frg", "Frg", base.DEC)
		local wnd   = ProtoField.uint16(NAME .. ".wnd", "Wnd", base.DEC)

		local ts    = ProtoField.uint32(NAME .. ".ts", "ts", base.DEC)
		local sn    = ProtoField.uint32(NAME .. ".sn", "sn", base.DEC)
		local una   = ProtoField.uint32(NAME .. ".una", "una", base.DEC)
		local len   = ProtoField.uint32(NAME .. ".len", "len", base.DEC)

		--[[
		  0               4   5   6       8 (BYTE)
		  +---------------+---+---+-------+
		  |     conv      |cmd|frg|  wnd  |
		  +---------------+---+---+-------+   8
		  |     ts        |     sn        |
		  +---------------+---------------+  16
		  |     una       |     len       |
		  +---------------+---------------+  24
		  |                               |
		  |        DATA (optional)        |
		  |                               |
		  +-------------------------------+
		--]]

		KCP.fields = {
			conv, cmd, frg, wnd,
			ts,        sn,
			una,       len
		}
		local data_dis = Dissector.get('data')
		KCP.dissector = function(Buffer, Menu, T)
			if Buffer:len() < KCP_HEADER_LENGTH then
				return false
			end

			local command = Buffer(4,1):uint()
			if command < 81 or command > 83 then
				return false
			end

			local Tree = T:add(KCP, Buffer())
			-- Registered Protocol Name
			Menu.cols.protocol = KCP.name
			-- Calculate the data offset value
			local offset  = 0

			local CONV =  Buffer(offset, 4)
			Tree:add_le(conv, CONV)
			Tree:append_text(", conv: " .. CONV:le_uint())
			offset = offset + 4


			local CMD =  Buffer(offset, 1)
			Tree:add_le(cmd, CMD)
			Tree:append_text(", cmd: " .. CMD_TO_STRING(CMD))
			offset = offset + 1

			local FRG =  Buffer(offset, 1)
			Tree:add_le(frg, FRG)
			Tree:append_text(", frg: " .. FRG:le_uint())
			offset = offset + 1

			local WND =  Buffer(offset, 2)
			Tree:add_le(wnd, WND)
			Tree:append_text(", wnd: " .. WND:le_uint())
			offset = offset + 2

			local TS =  Buffer(offset, 4)
			Tree:add_le(ts, TS)
			Tree:append_text(", ts: " .. TS:le_uint())
			offset = offset + 4

			local SN =  Buffer(offset, 4)
			Tree:add_le(sn, SN)
			Tree:append_text(", sn: " .. SN:le_uint())
			offset = offset + 4

			local UNA =  Buffer(offset, 4)
			Tree:add_le(una, UNA)
			Tree:append_text(", una: " .. UNA:le_uint())
			offset = offset + 4

			local LEN =  Buffer(offset, 4)
			Tree:add_le(len, LEN)
			Tree:append_text(", len: " .. LEN:le_uint())
			offset = offset + 4



			if CMD:le_uint() == 81 then
				local info = "PUSH, SN(" .. SN:le_uint() .. "), WND(".. WND:le_uint() ..")"
				if UNA:le_uint() > 0 then
					info = info .. "WAIT_SN(" .. UNA:le_uint() .. ")"
				end
				Menu.cols.info = info
			elseif CMD:le_uint() == 82 then
				Menu.cols.info = "ACK, SN(" .. SN:le_uint() .. "), NEXT_SN(" .. UNA:le_uint() .. "), WND(".. WND:le_uint() ..")"
			elseif CMD:le_uint() == 83 then
				-- TODO
			elseif CMD:le_uint() == 84 then
				-- TODO
			end

			local tcp_port_table = DissectorTable.get('tcp.port')
			game_dissector = tcp_port_table:get_dissector(serverTcpPort)
			game_dissector:call(Buffer(KCP_HEADER_LENGTH):tvb(), Menu, T)

			return true
		end
		return KCP
	end

	-- 对当前窗口进行
	local function filter_package()
		local filter = ''
		if serverTcpPort > 0 then
			filter = 'tcp.port == '.. serverTcpPort
		end
		if serverUdpPort > 0 then
			filter = filter .. " or udp.port == " .. serverUdpPort
		end
		set_filter(filter)
		apply_filter()
		-- reload_packets()
	end

	game_protocol_dissector = createGameProtocol("Game", "游戏协议")
	kcp_dissector = createKcpProtocolDissector("Kcp", "KCP协议")

	-- 输入触发
	local function dialog_triggle_func(tcpPort, kcpPort)
		if serverTcpPort > 0 then
			local tcp_port_table = DissectorTable.get('tcp.port')
			tcp_port_table:remove(serverTcpPort, game_protocol_dissector)
			serverTcpPort = 0
		end

		if serverUdpPort > 0 then
			local udp_port_table = DissectorTable.get('udp.port')
			udp_port_table:remove(serverUdpPort, kcp_dissector)
			serverUdpPort = 0
		end


		serverTcpPort = tonumber(tcpPort)
		if (kcpPort ~= nil and kcpPort ~= '') then
			serverUdpPort = tonumber(kcpPort)
		end

		local tcp_port_table = DissectorTable.get('tcp.port')
		tcp_port_table:add(serverTcpPort, game_protocol_dissector)
		print("Game Protocol Started")

		if serverUdpPort > 0 then
			local udp_port_table = DissectorTable.get('udp.port')
			udp_port_table:add(serverUdpPort, kcp_dissector)
			print("Kcp Protocol Started")
		end

		filter_package()
	end

	-- Define the menu entry's callback
	local function game_protocol_analyze()
		new_dialog("服务器端口输入",dialog_triggle_func,"服务器Tcp端口","服务器Kcp端口")
	end

	-- Create the menu entry
	register_menu("游戏协议分析",game_protocol_analyze, MENU_TOOLS_UNSORTED)
end
