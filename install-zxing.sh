#!/bin/sh
# Installs required ZXing libraries (http://code.google.com/p/zxing/) to local Maven repo.
# See http://code.google.com/p/zxing/issues/detail?id=88

cd /tmp

svn checkout http://zxing.googlecode.com/svn/trunk/core zxing-core
mvn -f zxing-core/pom.xml install

svn checkout http://zxing.googlecode.com/svn/trunk/javase zxing-javase
mvn -f zxing-javase/pom.xml install

rm -rf zxing-*
