package com.mytraining.javatraining.rdf

class Testr {
    static void main(String[] args) {
        def file = new File('/home/dev/IdeaProjects/java-training/data-structures/rdf/src/main/java/com/mytraining/javatraining/rdf/Dockerfile')
        def regex = ~'(?<=FROM docker-registry.ontotext.com/graphdb-ee:)[\\S]+'
        def newConfig = file.text.replaceFirst(regex,'sava')
        file.text = newConfig
    }
}