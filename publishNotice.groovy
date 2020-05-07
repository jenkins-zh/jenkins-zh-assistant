pipeline {
   agent {label 'master'}
   triggers {
        cron('H 5 * * *')
    }
   stages {
      stage('Hello') {
          steps {
                script{
                    sh "date  +%Y-%m-%d > today"
                    today = readFile('today').trim()
                    echo "${today}"
                    indexJson = readJSON file: '/var/www/jenkins-zh.github.io/index.json'
                    content = ""
                    indexValue = 0
                    for(i=0; i< indexJson.size(); i++ ){
                        if(indexJson[i].type=='wechat' && indexJson[i].date.contains(today) && indexJson[i].auhtor!=''){
                            echo "${indexJson[i].title} =========" + i
                            content += indexJson[i].uri+"\n"
                            indexValue++
                        }
                    }
                    if(indexValue!=0){
                        mail bcc: '', 
                        body: "共有${indexValue}篇文章需要发布，链接如下:\n${content}", 
                        cc: '', 
                        from: 'yJunS@jenkins-zh.cn', 
                        replyTo: 'media@jenkins-zh.cn', 
                        subject: "jenkins-zh(${today}) 共有${indexValue}篇文章需要发布", 
                        to: 'media@jenkins-zh.cn'
                    }
                    
                }
            }
        }
   }
}
