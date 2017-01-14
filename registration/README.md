Developers setup
================

AngularJS setup
---------------

In order to edit the AngularJS web gui, you have to install nodejs and npm in a
recent version. Don't use the version from the Ubuntu package sources, it is
very outdated!

To completely remove an older installation run

    sudo apt-get remove --purge nodejs npm

Install Node.js `v5*` via

    curl -sL https://deb.nodesource.com/setup_5.x | sudo -E bash -
    sudo apt-get install -y nodejs

If you need to add a new bower component, install bower via

    sudo npm install bower -g

Next, install Grunt components with

    sudo npm install -g grunt-cli

Navigate to the folder `src/main/angularjs` and execute the following commands
(**without sudo**):

    npm install grunt --save-dev
    npm install grunt-contrib-jshint --save-dev
    npm install jshint-stylish --save-dev
    npm install time-grunt --save-dev
    npm install jit-grunt --save-dev
    npm install grunt-contrib-copy --save-dev
    npm install grunt-contrib-clean --save-dev
    npm install grunt-contrib-concat --save-dev
    npm install grunt-contrib-cssmin --save-dev
    npm install grunt-contrib-uglify --save-dev
    npm install grunt-filerev --save-dev
    npm install grunt-usemin --save-dev
    npm install grunt-contrib-watch --save-dev
 
AngularJS Development
=====================

The AngularJS source files are located in the directory `src/main/angularjs`.
Any changes in `src/resources/static` will be overwritten by Grunt!

For development, open two terminals: in the first one, navigate to
`src/main/angularjs` and enter

    grunt watch

(Run just `grunt` the first time)

If the call fails with an error message

    /usr/bin/env: node: No such file or directory

execute

    sudo ln -s /usr/bin/nodejs /usr/bin/node

After saving any change to the projects resources, the whole files should be
deployed to `src/resources/static`.

In the second terminal, navigate to `registration` and enter

    mvn spring-boot:run

The application can be accessed at `http://localhost:8080`. 

After Grunt successfully built, the new resources are accessible without Sprint
Boot restart.

**Warning!** Template-Caching is used by AngularJS and it is nearly impossible
to be disabled in Firefox and other browsers. Sometimes, your in `*.html` files
changes aren't visible, even if Grunt ran successfully. It is recommended to use
Chrome with the following settings: With the developer tools opened, choose
their settings and check Disable cache (while DevTools is open).

