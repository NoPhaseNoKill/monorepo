#!/bin/bash

# Output file to save results
output_file="commit_analysis.txt"
> "$output_file" # Clear the file if it already exists

# List of commit hashes to analyze
commits=(
    ef92f14ae7c6867c15577e6ab56d98d9078fa494
    7218b7208adf750df1f96f7360d4c827892e0d9e
    6b1af8511bc003196f75c467a5bf49936714b288
    b96c93ea7c9d4544f48d824bb70fd38fdaadfedf
    e6e0c0b7705c9d1e7d96a743a95de1ba23bb3801
    86461dedbe5d0f84e427e2846eeeb558a6839894
    41103a580d34898f86befa530a5f0047c61750f5
    c79036abec9eda80bbe6962eda14867b2b04b4dc
    3f49763d0e33686d0197c8a39d575d98765d2aa6
    df673570615c1913a25ee21404e6f941169353fd
    803c24323f2366479663eadb80d2782af1721289
    93a6c7c9b362daf279f9829f825416c7d25519d5
    bc72a9529560f5f3d5e18dc5da3e18cd3d379ac4
    c1c7a8e2c1d8ef11004b0eff7c0662fe42e7bec1
    cb64f48b08334dc2ff981a529f17214b8c34d690
    3f1c80335c8a453224c44d61fc7ce60d82880426
    48da46bcc1af983e23273db08edf3aee6adaae20
    9741441748819904b10523f292ca0c5f2923c8d8
    86d043639fad7b5a86e6c7cbeaa480d3a312f24e
    dac6f784cbfb61cf710832ca851012d84976f3b9
    df9ecee3bd5cca4415e2c1c6cfc6dc77aa231f88
)

# Analyze each commit
for commit in "${commits[@]}"; do
    echo "Analyzing commit: $commit" >> "$output_file"
    echo "==================================" >> "$output_file"

    # Get the list of modified files
    files=$(git show --name-only --pretty="" "$commit")

    # Check each file for differences
    for file in $files; do
        if git diff --quiet "$commit -- $file" "$file"; then
            echo "File still matches commit: $file" >> "$output_file"
        else
            echo "File has diverged: $file" >> "$output_file"
        fi
    done

    echo "" >> "$output_file" # Add a blank line for readability
done

echo "Analysis complete. Results saved to $output_file"
