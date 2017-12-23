# gitlet
CS61B Project #3 - Gitlet
In this project you'll be implementing a version-control system that mimics some of the basic features of the popular system Git. Ours is smaller and simpler, however, so we have named it Gitlet.

A version-control system is essentially a backup system for related collections of files. The main functionality that Gitlet supports is:

1. Saving the contents of entire directories of files. In Gitlet, this is called committing, and the saved contents themselves are called commits.
2. Restoring a version of one or more files or entire commits. In Gitlet, this is called checking out those files or that commit.
3. Viewing the history of your backups. In Gitlet, you view this history in something called the log.
4. Maintaining related sequences of commits, called branches.
5. Merging changes made in one branch into another.
