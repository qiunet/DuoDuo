#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Redis

__author__ = 'qiunet'

import redis
from utils import CommonUtil
from utils import FileUtil

# 需要 pip3 install redis


class RedisClient(redis.Redis):
    def __init__(self, configFile='redis.properties'):
        """
        初始化 默认: redis.properties
        :param configFile: 文件名
        """
        config = FileUtil.Properties(configFile)
        password = None
        if not CommonUtil.isEmpty(config.get('pass')):
            password = config.get('pass')

        super(RedisClient, self).__init__(host=config.get('host'), port=config.get('port', 6379), password=password)

