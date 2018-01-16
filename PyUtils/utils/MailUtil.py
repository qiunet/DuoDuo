# -*- coding: utf-8 -*-

# 发送邮件

__author__ = 'qiunet'

import smtplib
import logging
from email.header import Header
from email.mime.text import MIMEText
from email.utils import parseaddr, formataddr


class MailObject:

    def __init__(self, mailFrom, fromAlias, smtpServer, password, smtpPort=25):
        """
        初始化函数
        :param mailFrom: 发送人地址
        :param fromAlias:  发送人别称
        :param smtpServer:  smtp服务器
        :param password:  密码

        :param smtpPort: smtp端口
        """
        self.__mailFrom = mailFrom
        self.__password = password
        self.__smtpPort = smtpPort
        self.__fromAlias = fromAlias
        self.__smtpServer = smtpServer
        pass

    def __formatAddr(self, str):
        name, addr = parseaddr(str)
        return formataddr((Header(name, 'Utf-8').encode(), addr))

    def sendMail(self, mailTo, subject, content):
        """
        发送邮件的方法
        :param mailTo: toAddr 发送到的邮箱地址 需要是数组
        :param subject: 标题
        :param content: 内容
        """
        mail = MIMEText(content, 'plain', 'Utf-8')
        mail['To'] = mailTo
        mail['Subject'] = Header(subject, 'UTF-8').encode()
        mail['From'] = self.__formatAddr(self.__fromAlias +' <%s>' % self.__mailFrom)

        try:
            server = smtplib.SMTP(self.__smtpServer, self.__smtpPort)

            server.set_debuglevel(1)
            server.login(self.__mailFrom, self.__password)
            server.sendmail(self.__mailFrom, mailTo, mail.as_string())
        except smtplib.SMTPException:
            logging.error("邮件发送失败")
        finally:
            server.quit()

