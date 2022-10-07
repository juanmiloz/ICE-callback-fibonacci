module Talker
{
    interface Callback{
        void response(string s);
    }
    interface Printer
    {
        void printString(string s, Callback* cl);
        void registerHost(string s, Callback* cl);
    }
}