#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# Comment

__author__ = 'qiunet'

import unittest
from utils import JsonUtil


class TestJsonFunc (unittest.TestCase) :

    def testToJsonString(self):
        testData = {"qiu1":"yang1", "qiu2": "yang2"}
        self.assertEqual("yang2", testData.get("qiu2"))
        jsonStr = JsonUtil.toJsonString(testData);
        self.assertEqual("{\"qiu1\": \"yang1\", \"qiu2\": \"yang2\"}", jsonStr)

        map = JsonUtil.getGeneralObject(jsonStr)
        print(type(map))
        self.assertEqual("yang2", map.get("qiu2"))

