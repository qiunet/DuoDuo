
import os
import unittest
from utils import FileUtil


class FileUtilFunc(unittest.TestCase):
    def filePath(self):
        """
        
        :return: 
        """
        fileName = os.path.join(os.getcwd(), "content.txt")
        self.assertEqual(os.getcwd(), FileUtil.dirname(fileName))

    def testWrite(self):
        """
        
        :return: 
        """
        fileName = os.path.join(os.getcwd(), "content.txt")
        exist = FileUtil.existFile(fileName)
        if exist:
            FileUtil.delFile(fileName)

        FileUtil.writeContent(fileName, "HelloWorld")
        FileUtil.writeContent(fileName, "HelloWorld")

        content = FileUtil.readContent(fileName)
        self.assertEqual(content, "HelloWorldHelloWorld")

        FileUtil.delFile(fileName)
        self.assertFalse(FileUtil.existFile(fileName))