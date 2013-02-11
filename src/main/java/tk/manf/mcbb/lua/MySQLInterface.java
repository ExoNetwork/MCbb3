package tk.manf.mcbb.lua;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import tk.manf.mcbb.api.database.MySQL;

public class MySQLInterface extends TwoArgFunction {
    private static MySQL database;

    public MySQLInterface() {}

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("newInstance", new iniciate());
        env.set("MySQL", library);
        return library;
    }

    static class iniciate extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue hostname, LuaValue port) {
            database = new MySQL(hostname.checkjstring(), port.checkjstring());
            return CoerceJavaToLua.coerce(database);
        }
    }
}
