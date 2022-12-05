import os

os.system(
    "javac @options ../src/util/* ../src/listDecorators/* ../src/transactions/* ../src/Accountable.java"
)
os.system(
    "jar cfm Accountable.jar ./META-INF/MANIFEST.MF ./bin ../../lib/javafx-sdk-19/lib ../src/resources"
)
