#hadoop-daemon.sh start journalnode
echo start jounalnode cluster
for i in hadoop5 hadoop6 hadoop7
do
#使用ssh进行启动
ssh heron@$i "/home/app/hadoop-2.6.5/sbin/hadoop-daemon.sh start journalnode"
done