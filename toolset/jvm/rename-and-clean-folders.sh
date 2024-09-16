#!/bin/bash
# Script to dynamically handle renaming, adding, and removing directories with / notation

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <old-path> <new-path>"
  exit 1
fi

old_path=$1
new_path=$2

echo "Starting folder renaming process..."
echo "Old path: $old_path"
echo "New path: $new_path"

# Function to handle renaming, adding, or removing subfolders
process_folders() {
    local old=$1
    local new=$2
    echo "Processing folders matching '$old'..."

    # Find directories matching the old path
    found_dirs=$(find . -type d -path "*/$old" 2>/dev/null)
    if [ -z "$found_dirs" ]; then
      echo "No directories found matching path '$old'."
    else
      echo "Directories found:"
      echo "$found_dirs"
      echo "$found_dirs" | while read -r old_dir; do
        new_dir=$(echo "$old_dir" | sed "s|$old|$new|")

        # Scenario 1: Same amount of directories (rename)
        if [ "$old_dir" == "$new_dir" ]; then
          echo "Renaming '$old_dir' to '$new_dir'."
          mv "$old_dir" "$new_dir" 2>/dev/null
          if [ $? -eq 0 ]; then
            echo "Successfully renamed '$old_dir' to '$new_dir'."
          else
            echo "Error renaming '$old_dir' to '$new_dir'."
          fi
        # Scenario 2: Removing a directory (move contents up)
        elif [[ "$new" == "$old"* ]]; then
          echo "Moving contents of '$old_dir' up one level to '$new_dir'."
          mkdir -p "$new_dir"
          mv "$old_dir"/* "$new_dir/" 2>/dev/null
          if [ $? -eq 0 ]; then
            echo "Successfully moved contents from '$old_dir' to '$new_dir'."
            rmdir "$old_dir" 2>/dev/null
            if [ $? -eq 0 ]; then
              echo "Successfully removed '$old_dir'."
            else
              echo "Could not remove '$old_dir' (not empty or permissions issue)."
            fi
          else
            echo "Error moving contents from '$old_dir' to '$new_dir'."
          fi
        # Scenario 3: Adding a directory (create new folder and move contents)
        elif [[ "$new" == */* ]]; then
          echo "Creating subfolder '$new_dir'."
          mkdir -p "$new_dir"
          echo "Moving contents from '$old_dir' to '$new_dir'."
          mv "$old_dir"/* "$new_dir/" 2>/dev/null
          if [ $? -eq 0 ]; then
            echo "Successfully moved contents from '$old_dir' to '$new_dir'."
            rmdir "$old_dir" 2>/dev/null
          else
            echo "Error moving contents to '$new_dir'."
          fi
        fi
      done
    fi
}

# Perform the operation to rename, remove, or add the folder
process_folders "$old_path" "$new_path"

echo "Folder processing completed."

