# mesh-rdf

## build it

```
$ sbt universal:stage
```

## run it

```
$ ./target/universal/stage/bin/pubmed-client fetch-articles -i 0885-7490 -f 'N-TRIPLES' -o mesh.nt
```
