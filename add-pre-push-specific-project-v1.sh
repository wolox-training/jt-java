#!/bin/bash -e

#***********************************************************************#
#                                                                       #
#                   add-pre-push-to-all-projects.sh                     #
#   This script adds a pre-push hook to the project from where it's     #
#   running. This script must be in the root project directory for it   #
#   to be able to access to the .git directory of the project.          #
#                                                                       #
#***********************************************************************#


# Go to the git template hooks directory
GIT_HOOKS_PATH=".git/hooks"
cd $GIT_HOOKS_PATH

# Create a custom pre-push and make it executable (If it exists, then an error is thrown)
if [ -e pre-push ]
  then printf "\npre-push already exists in $USER_HOOKS_PATH!\n\n"
  exit 1
fi

printf "\nCreating executable pre-push file\n"
touch pre-push
chmod +x pre-push

# Set the pre-push strategy (Supporting Maven and Gradle)
echo "#!/bin/bash
printf \"\nRunning pre-push hook\n\n\"
CWD=\`pwd\`
MAIN_DIR=\"\$( cd \"\$( dirname \"\${BASH_SOURCE[0]}\" )\" && pwd )\"
cd \$MAIN_DIR/../../
if [ -e pom.xml ]; then
  printf \"Maven project found!\n\n\"
  mvn clean test 2>/dev/null
  if [ \$? -ne 0 ]; then
    # If Maven doens't work, try with Maven wrapper
    ./mvnw clean test 2>/dev/null
    if [ \$? -ne 0 ]; then
      # Tests failed!
      printf \"\n\nMaven test failed, fix the problem before continue.\n\n\"
      cd \$CWD
      exit 1
    fi
  fi
  printf \"\n\nThe project compiles and the tests run OK!\n\n\"
  cd \$CWD
  exit 0
else
  if [ -e build.gradle ]; then
    printf \"Gradle project found!\n\n\"
    ./gradle clean test 2>/dev/null
    if [ \$? -ne 0 ]; then
      # If gradle doens't work, try with gradle wrapper
      ./gradlew clean test 2>/dev/null
      if [ \$? -ne 0 ]; then
        # Tests failed! Or gradlew is not set as executable (By default it's not)
        printf \"\nGradle test failed, fix the problem before continue. Is gradlew executable? (chmod +x ./gradlew)\n\n\"
        cd \$CWD
        exit 1
      fi
    fi
    printf \"\n\nThe project compiles and the tests run OK!\n\n\"
    cd \$CWD
    exit 0
  fi
fi
printf \"The current project is not a maven or gradle project, no pre-push hook was executed.\n\n\"" >> pre-push

printf "\npre-push file created! Now for this project a compilation and tests execution will be running before a new push.\n\n"
