if [[ "$TRAVIS_PULL_REQUEST" == "false" ]]
then
  if [[ "$TRAVIS_BRANCH" == "master" || "$TRAVIS_BRANCH" == "develop" ]]
  then
    echo -e "Starting to update circe-mvn-repo\n"

    cd ./target

    #go to home and setup git
    git config --global user.email "travis@travis-ci.org"
    git config --global user.name "Travis"

    #using token clone gh-pages branch
    git clone --quiet https://${GH_TOKEN}@github.com/tcoupin/circedroid-repo.git > /dev/null

    #go into diractory and copy data we're interested in to that directory
    cd circedroid-repo
    cp -f ../*.apk ./
    

    #add, commit and push files
    git add -f .
    git commit -m "Travis build $TRAVIS_BUILD_NUMBER pushed to circedroid-repo"
    git push -fq origin master > /dev/null

    echo -e "Done magic with coverage\n"
  fi
fi