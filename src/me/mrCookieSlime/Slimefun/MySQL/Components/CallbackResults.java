package me.mrCookieSlime.Slimefun.MySQL.Components;

import java.util.HashMap;
import java.util.List;

public abstract class CallbackResults {
    public CallbackResults()
    {

    }
    public void onResult(List<HashMap<String, ResultData>> results) {}
}
