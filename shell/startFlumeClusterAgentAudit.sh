usage="Usage: $0 (start|stop)"
if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi
behave=$1
echo "$behave flume cluster..."
#主机名称
for i in hadoop5 hadoop6 hadoop7
do
#使用ssh进行启动
script=""
if [ $1 == 'start' ];then
#script="flume-ng agent -n a1 -c /home/app/apache-flume-1.9.0-bin/conf -f /home/app/apache-flume-1.9.0-bin/conf/serverTokafka.conf -Dflume.root.logger=INFO,console"
ssh heron@$i "/home/app/apache-flume-1.9.0-bin/bin/flume-ng agent -n audit -c /home/app/apache-flume-1.9.0-bin/conf -f /home/app/apache-flume-1.9.0-bin/conf/kafka-to-hdfs-by-flume.conf >/home/app/apache-flume-1.9.0-bin/logs/audit/execlog 2>/home/app/apache-flume-1.9.0-bin/logs/audit/execerolog &"
fi
if [ $1 ==  'stop' ];then
ssh heron@$i "/home/app/stopFlume.sh"
fi
done
exit 0
	