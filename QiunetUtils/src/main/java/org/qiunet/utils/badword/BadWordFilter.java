package org.qiunet.utils.badword;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 涉及玩家输入的敏感词屏蔽
 * @Author qiunet
 * @Date Create in 2018/6/27 16:04
 **/
public enum  BadWordFilter {
	instance,;
	private INode rootNode;
	public static BadWordFilter getInstance() {
		return instance;
	}

	/***
	 * 加载
	 * @param badWords
	 */
	public void loadBadWord(IBadWord badWords) {
		rootNode = new RootNode();
		for (String badWord : badWords.getBadWordList()) {
			if (badWord.length() == 0) continue;

			int index = 0;
			INode node = rootNode;
			do {
				INode currNode = node.find(badWord.charAt(index));
				if (currNode == null) {
					node.addNode(node = new CharNode(badWord.charAt(index), index == (badWord.length() - 1)));
				}else {
					node = currNode;
				}
				// 重复的比如 麻痹  麻痹的 两个关键字 录取前面就ok
				if (node.endChar()) break;
			}while (++index < badWord.length());
		}
	}
	private static final String NOT_CHINESE_REGEX_STR = "[^\u4E00-\u9FA5]*?";
	/**非汉字的正则表达式*/
	private ThreadLocal<Pattern> NOT_CHINESE_REGEX = ThreadLocal.withInitial(() -> Pattern.compile("[^\u4E00-\u9FA5]*"));
	/**
	 * 屏蔽干扰字符. 比如习~近~平 仍然会被给出
	 * 如有敏感词返回相关的词，否则返回null
	 * @param content
	 * @return
	 */
	public String powerFind(String content) {
		Matcher m = NOT_CHINESE_REGEX.get().matcher(content);
		String tempContent = m.replaceAll("");
		String ret =  find(tempContent);
		if (ret == null
			&& tempContent.length() != content.length())
			ret = find(content);

		return ret;
	}

	/***
	 * 将content中的中文屏蔽字过滤. 即使中间有扰乱文字.
	 * @param content
	 * @return
	 */
	public String powerFilter(String content) {
		return powerFilter(content, "****");
	}

	public String powerFilter(String content, String replaceString) {
		Matcher m = NOT_CHINESE_REGEX.get().matcher(content);
		String tempContent = m.replaceAll("");
		String str;
		while ((str = find(tempContent)) != null) {
			StringJoiner joiner = new StringJoiner(NOT_CHINESE_REGEX_STR);
			for (char c : str.toCharArray()) {
				joiner.add(String.valueOf(c));
			}
			Matcher sm = Pattern.compile(joiner.toString()).matcher(content);
			tempContent = tempContent.replaceAll(str, "");
			content = sm.replaceAll(replaceString);
		}
		return content;
	}
	/**
	 * 如有敏感词返回相关的词，否则返回null
	 * @param str
	 * @return
	 */
	public String find(String str) {
		if (rootNode == null) {
			throw new NullPointerException("need loadBadWord first!");
		}

		if (str == null || (str = str.trim()).length() == 0) {
			return null;
		}

		int index = 0,startIndex = -1;
		INode node = rootNode;

		while (index < str.length()) {
			INode fnode = node.find(str.charAt(index));
			if(fnode != null){
				node = fnode;
				if (startIndex == -1) startIndex = index;
				if (node.endChar()) return str.substring(startIndex, index+1);
			}else{
				startIndex = -1;
				if(node instanceof CharNode){
					node = rootNode;
					continue;
				}
			}
			index ++;
		}

		return null;
	}
	/**
	 * 把敏感词替换成 相同数量的 *
	 * @param str
	 * @return
	 */
	public String doFilter(String str) {
		return doFilter(str, '*');
	}
	/**
	 * 把敏感词替换成 相同数量的 *
	 * @param str
	 * @return
	 */
	public String doFilter(String str, char replaceChar) {
		if (rootNode == null) {
			throw new NullPointerException("need loadBadWord first!");
		}

		if (str == null || (str = str.trim()).length() == 0) {
			return "";
		}

		char [] chars = str.toCharArray();
		int index = 0,startIndex = -1;
		INode node = rootNode;
		while (index < str.length()) {
			INode node0 = node.find(chars[index]);
			if (node0 != null) {
				node = node0;
				if (startIndex == -1) startIndex = index;
				if (node.endChar()) {
					for (int i = startIndex; i <= index; i++) {
						chars[i] = replaceChar;
					}
					startIndex = -1;
				}
			}else {
				startIndex = -1;
				if (node != rootNode &&
					(node = rootNode).find(chars[index]) != null)
					continue;
			}
			index ++;
		}
		return new String(chars);
	}

	interface INode {

		Character getChar();

		INode find(char c);

		void addNode(INode node);

		boolean endChar();
	}

	private static  class RootNode implements INode {
		private Map<Character, INode> nextNodes = new HashMap<>(512);

		@Override
		public Character getChar() {
			return null;
		}

		@Override
		public INode find(char c) {
			return nextNodes.get(c);
		}

		@Override
		public boolean endChar() {
			return false;
		}

		@Override
		public void addNode(INode node) {
			this.nextNodes.put(node.getChar(), node);
		}
	}

	private static class CharNode implements INode {
		private char c;
		private boolean endFlag;
		private List<INode> nextNodes = new LinkedList<>();

		public CharNode(char c, boolean endFlag) {
			this.c = c;
			this.endFlag = endFlag;
		}

		@Override
		public Character getChar() {
			return c;
		}

		@Override
		public INode find(char c) {
			for (INode node : nextNodes) {
				// 有cache
				if (node.getChar().equals(c)) {
					return node;
				}
			}
			return null;
		}

		@Override
		public void addNode(INode node) {
			this.nextNodes.add(node);
		}

		@Override
		public boolean endChar() {
			return endFlag;
		}
	}
}
