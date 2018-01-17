#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import os
import sys
from utils import MailUtil


__mailFrom = "qxy@i8dayou.com"
__fromAlias = "SVN备份监控邮件"
__password = "n4@xC2FB]7Mv"
__smtpServer = "smtp.exmail.qq.com"
__mailTo = ["qiunet@163.com"]
__ccTo = ["1503268025@qq.com", "qiunet@hotmail.com"]

mail = MailUtil.MailObject(__mailFrom, __fromAlias, __smtpServer, __password)
mail.sendMail(__mailTo, "Hello ", "WORLD", cc=__ccTo)
