cd /root/git_zlq/sellvideoproject;                                                                   \
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
/root/apache-tomcat-7.0.96/bin/startup.sh;sleep 10; \
mkdir /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/video;\
cp /root/apache-tomcat-7.0.96/webapps/f.mp4  /root/apache-tomcat-7.0.96/webapps/zhibing_mybatis/video