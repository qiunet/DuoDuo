#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# 简单实用mysql的工具类

__author__ = 'qiunet'

import os
import sys
import pymysql


class EasyMysql:
    __mysql_conn = None

    def __init__(self, host='127.0.0.1', port=3306, user='root', password='', db='', charset='utf8mb4', read_timeout=None, write_timeout=None, ssl=None):
        self.__mysql_conn = pymysql.connect(host=host, port=port, user=user, passwd=password, db=db, charset=charset, read_timeout=read_timeout, write_timeout=write_timeout, ssl=ssl)

    @staticmethod
    def __printTitle(description):
        for ele in description:
            print(ele[0].ljust(20), end='')
        print()

    def selectAndPrint(self, sql):
        """
        执行sql  打印结果
        :param sql:
        :return: 无
        """
        cursor = self.__mysql_conn.cursor()
        cursor.execute(sql)
        self.__printTitle(cursor.description)
        for sub in cursor.fetchall():
            for ele in sub:
                print(str(ele).ljust(20), end='')
            print()

    def select(self, sql):
        """
        执行sql
        :param sql:
        :return: 一个tuple
        """
        cursor = self.__mysql_conn.cursor()
        cursor.execute(sql)
        return cursor.fetchall()


edb = EasyMysql('127.0.0.1', 3306, 'root', 'qiuyang', 'test_0')
edb.selectAndPrint('select * from equip_a_0')
