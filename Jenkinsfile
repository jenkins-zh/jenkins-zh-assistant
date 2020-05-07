pipeline {
    agent any
    triggers {
        cron('H 1 * * *')
    }
    environment {
        translate = '翻译'
        original    = '原创'
    }
    
    stages {
        stage('notify') {
            steps {
                
                script {
                    genFile()
                    today = readFile('today').trim()
                    tomorrow = readFile('tomorrow').trim()
                    props = readJSON file: 'needReviewData.json'
                    approveDate = readJSON file 'needMergeData.json'
                    defaultValue = 0
                    content = ""
                    appValue = 0
                    contentData = ""
                    for(i=0; i< props.items.size(); i++ ){
                        if((props.items[i].title.contains(env.translate) && props.items[i].title.contains(today)) || (props.items[i].title.contains(env.translate) && props.items[i].title.contains(tomorrow)) || (props.items[i].title.contains(env.original) && props.items[i].title.contains(today)) || (props.items[i].title.contains(env.original) && props.items[i].title.contains(tomorrow))) {
                            content += "${props.items[i].html_url} \n"
                            defaultValue++
                        }
                        
                    }
                    for(i=0; i< approveDate.items.size(); i++ ){
                        if((approveDate.items[i].title.contains(env.translate) && approveDate.items[i].title.contains(today)) || (approveDate.items[i].title.contains(env.original) && approveDate.items[i].title.contains(today)) ) {
                            content += "${approveDate.items[i].html_url} \n"
                            appValue++
                        }
                        
                    }
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