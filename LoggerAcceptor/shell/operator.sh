#! /bin/bash
# qiunet
# qiunet@gmail.com
# 2017-09-24 12:08:50
# udp服务器的操作脚本
#
#---------------BODY-----------------
cd `dirname $0`

JAVA_OPTS="-server -Xmx512m -Xms512m -Xmn300m -Xss256k -XX:+UseParallelGC -XX:+UseParallelOldGC"
# udp 启动端口
port=8888

start(){
        if [ ! -d 'logs' ];then mkdir logs ; fi
        cd classes
        java ${JAVA_OPTS}  -classpath .:../lib/* org.qiunet.acceptor.server.BootStrap --port=${port} 2>&1 &
        pid=$!
        cd -
        echo $pid > pid
}

stop(){
        kill -TERM `cat pid`
        sleep 3
        rm pid
}

case "$1" in
        start)
                start
        ;;
        stop)
                stop
        ;;
        restart)
                stop
                start
        ;;
        *)
                echo "Usage:(start|stop|restart)"
                exit 0
        ;;
esac
