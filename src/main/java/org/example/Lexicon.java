package org.example;

public class Lexicon {
    final int TOKREC = 9;
    final int MAXTOKENS = 500;
    String[] lexemes = new String[500];
    String[] tokens = new String[500];
    String lexeme;
    int numTokens;
    int[] _i = new int[]{0};
    int initialToken;

    Automaton oAFD = new Automaton();

    public int getNumTokens() {
        return this.numTokens;
    }

    public String[] getTokens() {
        return this.tokens;
    }

    public String[] getLexemes() {
        return this.lexemes;
    }

    public void add(String tok, String lex) {
        this.tokens[this.numTokens] = tok;
        this.lexemes[this.numTokens++] = lex;
    }

    public Lexicon() {
        this._i[0] = 0;
        this.initialToken = 0;
        this.numTokens = 0;
    }

    private boolean isId() {
        String[] palRes = new String[]{"if", "else", "while", "public", "break", "int", "final", "switch", "double", "for", "int", "String"};

        for(int i = 0; i < palRes.length; ++i) {
            if (this.lexeme.equals(palRes[i])) {
                return false;
            }
        }

        return true;
    }

    public void start() {
        this._i[0] = 0;
        this.initialToken = 0;
        this.numTokens = 0;
    }

    public boolean analyze(String texto) {
        for(; this._i[0] < texto.length(); this.initialToken = this._i[0]) {
            boolean recAuto = false;
            int noAuto = 0;

            while(noAuto < 9 && !recAuto) {
                if (this.oAFD.recognize(texto, this.initialToken, this._i, noAuto)) {
                    recAuto = true;
                } else {
                    ++noAuto;
                }
            }

            if (!recAuto) {
                return false;
            }

            this.lexeme = texto.substring(this.initialToken, this._i[0]);
            switch (noAuto) {
                case 0:
                default:
                    break;
                case 1:
                    if (this.isId()) {
                        this.tokens[this.numTokens] = "id";
                    } else {
                        this.tokens[this.numTokens] = this.lexeme;
                    }
                    break;
                case 2:
                    this.tokens[this.numTokens] = this.lexeme;
                    break;
                case 3:
                    this.tokens[this.numTokens] = this.lexeme;
                    break;
                case 4:
                    this.tokens[this.numTokens] = "num";
                    break;
                case 5:
                    this.tokens[this.numTokens] = this.lexeme;
                    break;
                case 6:
                    this.tokens[this.numTokens] = this.lexeme;
                    break;
                case 7:
                    this.tokens[this.numTokens] = "num";
                    break;
                case 8:
                    this.tokens[this.numTokens] = "num";
            }

            if (noAuto > 0) {
                this.lexemes[this.numTokens++] = this.lexeme;
            }
        }

        return true;
    }
}
