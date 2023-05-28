package org.example;

public class Lexicon {
    final int TOKREC = 9;
    final int MAXTOKENS = 500;
    String[] _lexemas = new String[500];
    String[] _tokens = new String[500];
    String _lexema;
    int _noTokens;
    int[] _i = new int[]{0};
    int _iniToken;

    Automaton oAFD = new Automaton();

    public int noTokens() {
        return this._noTokens;
    }

    public String[] Tokens() {
        return this._tokens;
    }

    public String[] Lexemas() {
        return this._lexemas;
    }

    public void add(String tok, String lex) {
        this._tokens[this._noTokens] = tok;
        this._lexemas[this._noTokens++] = lex;
    }

    public Lexicon() {
        this._i[0] = 0;
        this._iniToken = 0;
        this._noTokens = 0;
    }

    private boolean EsId() {
        String[] palRes = new String[]{"if", "else", "while", "public", "break", "int", "final", "switch", "double", "for", "int", "String"};

        for(int i = 0; i < palRes.length; ++i) {
            if (this._lexema.equals(palRes[i])) {
                return false;
            }
        }

        return true;
    }

    public void Start() {
        this._i[0] = 0;
        this._iniToken = 0;
        this._noTokens = 0;
    }

    public boolean Analyze(String texto) {
        for(; this._i[0] < texto.length(); this._iniToken = this._i[0]) {
            boolean recAuto = false;
            int noAuto = 0;

            while(noAuto < 9 && !recAuto) {
                if (this.oAFD.recognize(texto, this._iniToken, this._i, noAuto)) {
                    recAuto = true;
                } else {
                    ++noAuto;
                }
            }

            if (!recAuto) {
                return false;
            }

            this._lexema = texto.substring(this._iniToken, this._i[0]);
            switch (noAuto) {
                case 0:
                default:
                    break;
                case 1:
                    if (this.EsId()) {
                        this._tokens[this._noTokens] = "id";
                    } else {
                        this._tokens[this._noTokens] = this._lexema;
                    }
                    break;
                case 2:
                    this._tokens[this._noTokens] = this._lexema;
                    break;
                case 3:
                    this._tokens[this._noTokens] = this._lexema;
                    break;
                case 4:
                    this._tokens[this._noTokens] = "num";
                    break;
                case 5:
                    this._tokens[this._noTokens] = this._lexema;
                    break;
                case 6:
                    this._tokens[this._noTokens] = this._lexema;
                    break;
                case 7:
                    this._tokens[this._noTokens] = "num";
                    break;
                case 8:
                    this._tokens[this._noTokens] = "num";
            }

            if (noAuto > 0) {
                this._lexemas[this._noTokens++] = this._lexema;
            }
        }

        return true;
    }
}
