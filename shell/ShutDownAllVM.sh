for i in hadoop2 hadoop3 hadoop4 hadoop5 hadoop6 hadoop7 hadoop1
do
ssh root@$i "shutdown -h now"
done