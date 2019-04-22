usage="Usage: $0 (start|stop)"
if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi
behave=$1
echo "$behave kafka cluster..."
#主机名称
for i in hadoop5 hadoop6 hadoop7
do
#使用ssh进行启动
script=""
if [ $1 == 'start' ];then
script="kafka-server-start.sh /home/app/kafka_2.11-2.1.1/config/server.properties 1>/home/app/kafka_2.11-2.1.1/kafkalogs/kafka.out 2>&1 &"
fi
if [ $1 ==  'stop' ];then
script="kafka-server-stop.sh"
fi
ssh heron@$i "/home/app/kafka_2.11-2.1.1/bin/$script"
done
exit 0
	