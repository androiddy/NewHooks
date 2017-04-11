# NewHooks
基于YAHFA和dexposed 结合的hook框架  支持4.0-4.4  5.1-6.0


 HookResult andHookMethod = DalvikArt.findAndHookMethod(application,
                MainActivity.class, "Toasts", TestProxy1.class, void.class, boolean[].class,
                String.class, View[].class, new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Toast.makeText(App.getContext(), param.args[1] + " Hook1", 0).show();
                        return null;
                    }
                });
