package net.lbd01.tiny.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TinyJsonTool {

    //defined to avoid byte to char conversion
    private static final byte _space = ' ';
    private static final byte _tab = '\t';
    private static final byte _lf = 0x0A;
    private static final byte _cr = 0x0D;
    private static final byte _quote = '"';
    private static final byte _backslash = '\\';
    private static final byte _slash = '/';
    private static final byte _colon = ':';
    private static final byte _comma = ',';
    private static final byte _dot = '.';
    private static final byte _minus = '-';
    private static final byte _plus = '+';
    private static final byte _curly_bracket_left = '{';
    private static final byte _curly_bracket_right = '}';
    private static final byte _square_bracket_left = '[';
    private static final byte _square_bracket_right = ']';
    private static final byte _double_quote = '"';
    private static final byte _a = 'a';
    private static final byte _b = 'b';
    private static final byte _e = 'e';
    private static final byte _E = 'E';
    private static final byte _f = 'f';
    private static final byte _l = 'l';
    private static final byte _n = 'n';
    private static final byte _s = 's';
    private static final byte _r = 'r';
    private static final byte _t = 't';
    private static final byte _u = 'u';

    static class Ctx {
        byte[] txt;
        int idx = 0;
    }

    public static JsonObj parseObject(String json) {
        return parseObject(json,false);
    }

    public static JsonObj parseObject(String str, boolean dontConvertBytesToUtf8) {
        if (str==null || (str = str.trim()).length()<2) throw new JsonParseException("Invalid json object");
        Ctx ctx = new Ctx();
        if (dontConvertBytesToUtf8) ctx.txt = str.getBytes();
        else ctx.txt = str.getBytes(StandardCharsets.UTF_8);
        return parseObject(ctx);
    }

    private static JsonObj parseObject(Ctx ctx) {
        findCharacter(ctx,_curly_bracket_left);
        JsonObj object = new JsonObj();
        boolean noNewFieldExpected = true;
        while (ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (isWhitespace(c)) {
                ++ctx.idx;
            } else if (c == _curly_bracket_right) {
                ++ctx.idx;
                break;
            } else {
                String fieldName = parseString(ctx);
                findCharacter(ctx,_colon);
                JsonVal fieldValue = parseFieldValue(ctx);
                object.set(fieldName,fieldValue);
                if ( (noNewFieldExpected = findCharacterOrComma(ctx,_curly_bracket_right)) ) break;
            }
        }
        if (!noNewFieldExpected) throw new JsonParseException("Expected object field not found");
        return object;
    }

    private static String parseString(Ctx ctx) {
        boolean started = false;
        boolean completed = false;
        boolean escapeStarted = false;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        while (!completed && ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (!started) {
                if (isWhitespace(c)) {
                    //skip
                } else if (c==_double_quote) {
                    started = true;
                } else {
                    throw new JsonParseException("Invalid character at: " + ctx.idx);
                }
            } else {
                if (escapeStarted) {
                    handleEscapeCharacter(c,stream,ctx);
                    escapeStarted = false;
                } else if (c==_backslash) {
                    escapeStarted = true;
                } else if (c==_double_quote) {
                    completed = true;
                } else {
                    stream.write(c);
                }
            }
            ++ctx.idx;
        }
        return stream.toString();
    }

    private static void handleEscapeCharacter(byte c, ByteArrayOutputStream stream, Ctx ctx) {
        if (c == _quote || c == _slash || c == _backslash) {
            stream.write(c);
        } else if (c == _b) {
            stream.write('\b');
        } else if (c == _f) {
            stream.write('\f');
        } else if (c == _n) {
            stream.write('\n');
        } else if (c == _r) {
            stream.write('\r');
        } else if (c == _t) {
            stream.write('\t');
        } else if (c == _u) {
            if (ctx.txt.length-ctx.idx>3) {
                try {
                    char unicodeChar = (char)Integer.parseInt(
                            new String(new byte[]{ctx.txt[ctx.idx+1],ctx.txt[ctx.idx+2],ctx.txt[ctx.idx+3],ctx.txt[ctx.idx+4]}),
                            16);
                    stream.write(String.valueOf(unicodeChar).getBytes(StandardCharsets.UTF_8));
                    ctx.idx+=4;
                } catch (NumberFormatException | IOException ex) {
                    throw new JsonParseException("Invalid unicode sign at: " + ctx.idx);
                }
            } else {
                throw new JsonParseException("Missing unicode chars at: " + ctx.idx);
            }
        } else {
            throw new JsonParseException("Invalid escape character: " + ((char)c));
        }
    }

    private static boolean isWhitespace(byte c) {
        return c == _space || c == _tab || c == _lf || c == _cr;
    }

    private static boolean isDigit(byte c) {
        return c>47 && c<58;
    }

    private static void findCharacter(Ctx ctx, byte charToFind) {
        while (ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (c == charToFind) {
                ++ctx.idx;
                return;
            } else if (isWhitespace(c)) {
                ++ctx.idx;
            } else {
                throw new JsonParseException("Invalid character at " + ctx.idx + " / " + new String(ctx.txt).substring(ctx.idx));
            }
        }
        throw new JsonParseException("Character not found: " + charToFind + " in: " + new String(ctx.txt).substring(ctx.idx));
    }

    private static boolean findCharacterOrComma(Ctx ctx, byte charToFind) {
        while (ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (c == charToFind || c == _comma) {
                ++ctx.idx;
                return c==charToFind;
            } else if (isWhitespace(c)) {
                ++ctx.idx;
            } else {
                throw new JsonParseException("Invalid character found at: " + ctx.idx + " / " + new String(ctx.txt).substring(ctx.idx));
            }
        }
        throw new JsonParseException("Character " + charToFind + " / comma not found in: " + new String(ctx.txt).substring(ctx.idx));
    }

    private static JsonVal parseFieldValue(Ctx ctx) {
        while (ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (c == _square_bracket_left) {
                return JsonVal.array(parseArray(ctx));
            } else if (c == _curly_bracket_left) {
                return JsonVal.object(parseObject(ctx));
            } else if (c == _double_quote) {
                return JsonVal.string(parseString(ctx));
            } else if (isDigit(c) || c==_minus) {
                return parseNumber(ctx);
            } else if (c==_t || c==_f) {
                return JsonVal.bool(parseBoolean(ctx));
            } else if (c==_n) {
                return parseNull(ctx);
            } else if (isWhitespace(c)) {
                //skip
                ++ctx.idx;
            } else {
                throw new JsonParseException("Invalid field delimeter at: " + ctx.idx);
            }
        }
        throw new JsonParseException("No field value found in: " + new String(ctx.txt).substring(ctx.idx));
    }


    private static JsonVal parseNumber(Ctx ctx) {
        boolean decimalPoint = false;
        int startIdx = ctx.idx;
        if (ctx.txt[ctx.idx]=='-') ++ctx.idx;
        while (ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (isDigit(c) || c==_E || c==_e || c==_plus || c==_minus) { // number + exponent
            } else if (c==_dot) {
                decimalPoint = true;
            } else if (isWhitespace(c) || c== _comma || c==_square_bracket_right || c==_curly_bracket_right) {
                break;
            }
            ++ctx.idx;
        }

        try {
            if (decimalPoint) {
                return JsonVal.decimal(new BigDecimal(new String(ctx.txt,startIdx,ctx.idx-startIdx)));
            } else {
                return JsonVal.integer(Integer.valueOf(new String(ctx.txt,startIdx,ctx.idx-startIdx)));
            }
        } catch (NumberFormatException ex) {
            throw new JsonParseException("Not a number: " + new String(ctx.txt,startIdx,ctx.idx-startIdx) + " at: " + startIdx);
        }
    }

    private static boolean parseBoolean(Ctx ctx) {
        if (ctx.txt.length-ctx.idx>3 && ctx.txt[ctx.idx]==_t && ctx.txt[ctx.idx+1]==_r && ctx.txt[ctx.idx+2]==_u && ctx.txt[ctx.idx+3]==_e) {
            ctx.idx+=4;
            return true;
        }
        if (ctx.txt.length-ctx.idx>4 && ctx.txt[ctx.idx]==_f && ctx.txt[ctx.idx+1]==_a && ctx.txt[ctx.idx+2]==_l && ctx.txt[ctx.idx+3]==_s && ctx.txt[ctx.idx+4]==_e) {
            ctx.idx+=5;
            return false;
        }
        throw new JsonParseException("Value not 'true'/'false' at: " + ctx.idx);
    }

    private static JsonVal parseNull(Ctx ctx) {
        if (ctx.txt.length-ctx.idx>3 && ctx.txt[ctx.idx]==_n && ctx.txt[ctx.idx+1]==_u && ctx.txt[ctx.idx+2]==_l && ctx.txt[ctx.idx+3]==_l) {
            ctx.idx+=4;
            return null;
        }
        throw new JsonParseException("Value not 'null' at: " + ctx.idx);
    }

    private static List<JsonVal> parseArray(Ctx ctx) {
        findCharacter(ctx,_square_bracket_left);
        List<JsonVal> items = new ArrayList<>();
        boolean noNewItemExpected = true;
        while (ctx.idx<ctx.txt.length) {
            byte c = ctx.txt[ctx.idx]; //trick: in utf8 ascii have single byte representation
            if (isWhitespace(c)) {
                ++ctx.idx;
            } else if (c == _square_bracket_right) {
                ++ctx.idx;
                break;
            } else {
                if (c == _square_bracket_left) {
                    items.add(JsonVal.array(parseArray(ctx)));
                } else if (c == _curly_bracket_left) {
                    items.add(JsonVal.object(parseObject(ctx)));
                } else if (c == _double_quote) {
                    items.add(JsonVal.string(parseString(ctx)));
                } else if (isDigit(c) || c==_minus) {
                    items.add(parseNumber(ctx));
                } else if (c==_t || c==_f) {
                    items.add(JsonVal.bool(parseBoolean(ctx)));
                } else if (c==_n) {
                    items.add(parseNull(ctx));
                } else {
                    throw new JsonParseException("Invalid array item at: " + ctx.idx);
                }
                if ( (noNewItemExpected = findCharacterOrComma(ctx,_square_bracket_right)) ) break;
            }
        }
        if (!noNewItemExpected) throw new JsonParseException("Expected next array item not found");
        return items;
    }

}
