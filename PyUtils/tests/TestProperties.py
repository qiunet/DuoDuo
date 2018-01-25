#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import unittest
from utils.FileUtil import Properties


class PropertiesTest(unittest.TestCase):

    def testProperties(self):
        self.data = Properties("config.properties")
        self.assertEqual(True, self.data.containKey('qiu1'))
        self.assertEqual(False, self.data.containKey('qiu0'))
        self.assertEqual('qiuyang1', self.data.get('qiu1'))
        self.assertNotEqual('qiuyang1', self.data.get('qiu2'))

