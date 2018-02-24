
import os
import unittest
from utils import FileUtil


class FileUtilFunc(unittest.TestCase):
    def testWrite(self):
        """
        
        :return: 
        """
        fileName = os.getcwd()+os.path.sep+"content.txt"
        exist = FileUtil.existFile(fileName)
        if exist:
            FileUtil.delFile(fileName)

        FileUtil.writeContent(fileName, "HelloWorld")
        FileUtil.writeContent(fileName, "HelloWorld")

        content = FileUtil.readContent(fileName)
        self.assertEqual(content, "HelloWorldHelloWorld")

        FileUtil.delFile(fileName)
        self.assertFalse(FileUtil.existFile(fileName))