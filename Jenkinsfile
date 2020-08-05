pipeline {
    agent any
    triggers {
        cron('H 1 * * *')
    }
    environment {
        translate = '翻译'
        original = '原创'
    }
    
    stages {
        stage('notify') {
            steps {
                
                script {
                    genFile()
                    today = readFile('today').trim()
                    echo "today: ${today}"
                    tomorrow = readFile('tomorrow').trim()
                    echo "tomorrow: ${tomorrow}"
                    props = readJSON file: 'needReviewData.json'
                    echo "props: ${props}"
                    sh "pwd"
                    sh "ls -ahl |grep .json"
                    approveData = readJSON file: 'needMergeData.json'
                    echo "approveData: ${approveData}"
                    defaultValue = 0
                    content = ""
                    appValue = 0
                    contentData = ""
                    echo "props.total_count: ${props.total_count}"
                    if(props.total_count != 0){
                        for(i=0; i< props.items.size(); i++ ){
                            if((props.items[i].title.contains(env.translate) && props.items[i].title.contains(today)) || (props.items[i].title.contains(env.translate) && props.items[i].title.contains(tomorrow)) || (props.items[i].title.contains(env.original) && props.items[i].title.contains(today)) || (props.items[i].title.contains(env.original) && props.items[i].title.contains(tomorrow))) {
                                content += "${props.items[i].html_url} \n"
                                defaultValue++
                            }
                            
                        }
                    }
                    echo "content: ${content}"
                    echo "approveData.total_count: ${approveData.total_count}"
                    if(approveData.total_count !=0){
                        for(i=0; i< approveData.items.size(); i++ ){
                            if((approveData.items[i].title.contains(env.translate) && approveData.items[i].title.contains(today)) || (approveData.items[i].title.contains(env.original) && approveData.items[i].title.contains(today)) ) {
                                contentData += "${approveData.items[i].html_url} \n"
                                appValue++
                            }
                            
                        }
                    }
                    echo "contentData: ${contentData}"
                    sendEmail(defaultValue,content,today,appValue,contentData)
                }
                
            }
        }
    }
}

def genFile(){
    sh "date  +%Y-%m-%d > today"
    sh "date +%Y-%m-%d --date='1 day'  > tomorrow" //mac is `date -v +1d +%Y-%m-%d`
    sh 'curl -s https://api.github.com/search/issues?q=repo:jenkins-zh/jenkins-zh+type:pr+is:open+team-review-requested:social-media-admin+review:none > needReviewData.json'
    sh 'curl -s https://api.github.com/search/issues?q=repo:jenkins-zh/jenkins-zh+type:pr+is:open+team-review-requested:social-media-admin+review:approved > needMergeData.json'
}

def sendEmail(int defaultValue,String content,String today,int appValue,String contentData){
    if(defaultValue != 0 && appValue != 0) {
        mail bcc: '', 
        body: "共有${defaultValue}篇文章需要review，链接如下:\n${content}\n共有${appValue}篇文章需要merge,链接如下:\n${contentData}", 
        cc: '', 
        from: 'yJunS@jenkins-zh.cn', 
        replyTo: 'media@jenkins-zh.cn', 
        subject: "jenkins-zh(${today})文章merge/review提醒", 
        to: 'media@jenkins-zh.cn'
    } else if (defaultValue != 0) {
        mail bcc: '', 
        body: "共有${defaultValue}篇文章需要review，链接如下:\n${content}",
        cc: '', 
        from: 'yJunS@jenkins-zh.cn', 
        replyTo: 'media@jenkins-zh.cn', 
        subject: "jenkins-zh(${today})文章merge/review提醒", 
        to: 'media@jenkins-zh.cn'
    } else if (appValue != 0) {
        mail bcc: '', 
        body: "共有${appValue}篇文章需要merge,链接如下:\n${contentData}", 
        cc: '', 
        from: 'yJunS@jenkins-zh.cn', 
        replyTo: 'media@jenkins-zh.cn', 
        subject: "jenkins-zh(${today})文章merge/review提醒", 
        to: 'media@jenkins-zh.cn'
    }
}