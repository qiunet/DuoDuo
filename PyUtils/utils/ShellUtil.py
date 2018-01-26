# -*- coding: utf-8 -*-
# 系统命令相关的python功能.

import subprocess

__author__ = 'qiunet'


def execShell(cmd: str, *args, timeout=None) -> int:
    """
    执行shell 命令
    :param cmd: 命令
    :param args: 参数
    :param timeout: 指定超时 秒
    :return: 0 返回正确 其它错误
    """
    return subprocess.call([cmd, *args], shell=True, timeout=timeout)

