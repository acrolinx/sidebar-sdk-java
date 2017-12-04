/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused"})
class JSConsole
{
    private final Logger logger = LoggerFactory.getLogger(JSConsole.class);

    public void log(final String s)
    {
        logger.debug("SidebarLogs: " + s);
    }

    public void error(final String s)
    {
        logger.debug("SidebarError: " + s);
    }

    public static String overwriteJSLogging()
    {
        return "(function(){\n" + "  if(window.console && console.log){\n" + "    var old = console.log;\n"
                + "    var oldError = console.error;\n" + "    console.log = function(){\n"
                + "      old.apply(this, arguments);\n" + "      var args = [].slice.apply(arguments);\n"
                + "      var msg = '';\n" + "      args.forEach(function(arg){\n"
                + "        msg += JSON.stringify(arg)\n" + "      })\n" + "      if(window.java) {\n"
                + "        java.log(msg);\n" + "      }\n" + "    }\n" + "    console.error = function(){\n"
                + "      oldError.apply(this, arguments);\n" + "      var args = [].slice.apply(arguments);\n"
                + "      var msg = '';\n" + "      args.forEach(function(arg){\n"
                + "        msg += JSON.stringify(arg)\n" + "      })\n" + "      if(window.java) {\n"
                + "        java.error(msg);\n" + "      }\n" + "    }\n" + "  }\n" + "})();";
    }
}
