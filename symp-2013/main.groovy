import com.liferay.portal.kernel.scripting.ScriptingUtil;
String baseURL = "https://raw.github.com/dsanz/scripts/master/symp-2013/"

String framework = new URL(baseURL + "util/FrameworkLoader.groovy").text
String launcher = new URL(baseURL + "util/launcher.groovy").text

launcher = launcher.replace("@", "util/ClusterMonitorSample.groovy")

Map<String, Object> env = new HashMap<String, Object>();
env.put("out", out);

//That method is deprecated in Liferay 6.2 GA2, only valid in 6.2-RC4
ScriptingUtil.exec(null, env, "groovy", framework + "\n" + launcher);
