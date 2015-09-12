# LexisNexisParser
A Java parser to read LexisNexis documents and create POJOs from them. 

To parse the contents of a given file, you can write the following:

```java
LexisNexisParser parser = new LexisNexisParser();
File lnFile = new File(lnFilePath).getAbsoluteFile();
List<LexisNexisDocument> lnDocs = parser.parse(lnFile);

LexisNexisDocument firstDoc = lnDocs.get(0);
```

Note that if the source file contains several LexisNexis documents, one LexisNexisDocument instance will be create per document in the file. 

As regarding the Unit test, I am not able to include the test documents in the repo, due to T&C. So, the Unit test won't run, but I might create some data at some stage.  


		
