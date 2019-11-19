1.yum install jdk

	yum install -y java;
	yum install -y maven;
2.apache-tomcat-7.0.96  解压缩版
	cd /;
	wget http://ftp.wayne.edu/apache/tomcat/tomcat-7/v7.0.96/bin/apache-tomcat-7.0.96.tar.gz;
	tar -xzf apache-tomcat-7.0.96.tar.gz;

3.centos7  install  redis 5
   #https://www.cnblogs.com/autohome7390/p/6433956.html
   #https://www.cnblogs.com/linjiqin/p/7965783.html
   #https://blog.csdn.net/CrazyLai1996/article/details/76472856
 
	yum install -y http://rpms.famillecollet.com/enterprise/remi-release-7.rpm;
	yum --enablerepo=remi install redis -y;
	service redis start;

mkdir /root/git_zlq/;
cd /root/git_zlq/;
git clone https://github.com/zlqlpp/sellvideoproject.git;

######## ~/.sysconfig
alias go='cd /root/git_zlq/sellvideoproject;                                                         \
git pull;                                                                                            \
mvn clean package;                                                                                   \
/root/apache-tomcat-7.0.96/bin/shutdown.sh;                                                          \
rm -rf /root/apache-tomcat-7.0.96/webapps/zhi*;                                                      \
rm -rf /root/apache-tomcat-7.0.96/webapps/ROOT;                                                      \
rm -rf /root/apache-tomcat-7.0.96/logs/*;                                                            \
cp /root/git_zlq/sellvideoproject/target/zhibing_mybatis.war /root/apache-tomcat-7.0.96/webapps;     \
rm -rf /root/apache-tomcat-7.0.96/work/*;                                                            \
rm -rf /root/apache-tomcat-7.0.96/temp/*;                                                            \
rm -rf /var/lib/tomcat/webapps/*;                                                                    \
/root/apache-tomcat-7.0.96/bin/startup.sh;sleep 15; \
mkdir /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/video;\
touch /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/passwd.txt;\
chmod 777 /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/passwd.txt;\
touch /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/video.txt;\
chmod 777 /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/video.txt;\
vlog; '

alias cdapp='cd /root/apache-tomcat-7.0.96/webapps'
alias cdlog='cd /root/apache-tomcat-7.0.96/logs'
alias vlog="tail -f /root/apache-tomcat-7.0.96/logs/catalina.out"


#######.bash_profile
alias welcome='clear;source ~/.bash_profile'
source /root/.sysconfig


 source ~/.bash_profile






修改tomcat  端口80 
host 修改成 域名
<Context path="/" docBase="/root/apache-tomcat-7.0.96/webapps/zhibing_mybatis"  reloadable="true" />
