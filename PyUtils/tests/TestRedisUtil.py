#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

from data.redis import RedisUtil
import unittest


class RedisUtilTest(unittest.TestCase):
    def test(self):
        redisClient = RedisUtil.RedisClient()
        bool = redisClient.set("qiu1", "qiuyang")
        if bool:
            self.assertEqual("qiuyang", bytes(redisClient.get("qiu1")).decode("utf-8"))

