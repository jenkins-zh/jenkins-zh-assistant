# jenkins-zh-assistant
社区网站助理

## 作用

### Jenkinsfile
此文件定时获取需要 Review(当天和后一天) 和 已经 Review 需要 merge(当天) 的文章按照类别罗列出来，统一发邮件

### publishNotice.groovy
此文件定时获取当天已合并且需要发布到公众号的文章，统一发邮件