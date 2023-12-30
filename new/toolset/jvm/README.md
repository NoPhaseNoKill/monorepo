
## Running the gradle profiler

```
EPOCH_TIME=$(date +%s) &&
gradle-profiler --benchmark --scenario-file ./gradle-profiler/scenarios/performance.scenarios --output-dir "./gradle-profiler/$EPOCH_TIME" --gradle-user-home './gradle-profiler/gradle-user-home' performance_both &&
unset EPOCH_TIME
```


