usage="Usage: $0 (start|stop)"
if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi
behave=$1
echo "$behave zookeeper cluster..."
#主机名称
for i in hadoop5 hadoop6 hadoop7
do
#使用ssh进行启动
script=""
if [ $1 == 'start' ];then
script="zkServer.sh start"
fi
if [ $1 ==  'stop' ];then
script="zkServer.sh stop"
fi
ssh heron@$i "/home/app/zookeeper-3.4.5/bin/$script"
done
exit 0
	