package com.lemoulinstudio.gfa.analysis;

import com.lemoulinstudio.gfa.core.cpu.Arm7Tdmi;
import com.lemoulinstudio.gfa.core.cpu.ArmReg;
import com.lemoulinstudio.gfa.core.memory.GfaMMU;
import com.lemoulinstudio.gfa.core.time.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

  private Map<String, ScmExpr> environment;
  private GfaMMU memory;
  private ArmReg[][] allRegisters;
  private Time time;

  private void initEnvironment() {
    environment = new HashMap<String, ScmExpr>();
    
    define("#t", new BoolTrue());
    define("#f", new BoolFalse());

    try {
      define("r0", new IntReg(getRegister("r0", null)));
      define("r1", new IntReg(getRegister("r1", null)));
      define("r2", new IntReg(getRegister("r2", null)));
      define("r3", new IntReg(getRegister("r3", null)));
      define("r4", new IntReg(getRegister("r4", null)));
      define("r5", new IntReg(getRegister("r5", null)));
      define("r6", new IntReg(getRegister("r6", null)));
      define("r7", new IntReg(getRegister("r7", null)));
      define("r8", new IntReg(getRegister("r8", null)));
      define("r9", new IntReg(getRegister("r9", null)));
      define("r10", new IntReg(getRegister("r10", null)));
      define("r11", new IntReg(getRegister("r11", null)));
      define("r12", new IntReg(getRegister("r12", null)));
      define("r13", new IntReg(getRegister("r13", null)));
      define("r14", new IntReg(getRegister("r14", null)));
      define("r15", new IntReg(getRegister("r15", null)));
      define("cpsr", new IntReg(getRegister("cpsr", null)));
      define("spsr", new IntReg(getRegister("spsr", null)));
      define("sp", new IntReg(getRegister("sp", null)));
      define("lr", new IntReg(getRegister("lr", null)));
      define("pc", new IntReg(getRegister("pc", null)));
    }
    catch (ParseException ex) {}
  }

  public boolean isValid(String sourceCode) {
    try {
      parse(sourceCode, null, null, null);
      return true;
    }
    catch (ParseException ex) {
      return false;
    }
  }

  /**
   * Translate a String into a BoolExpr object
   * witch can be used for some code analysis.
   */
  public BoolExpr parse(String sourceCode,
                        GfaMMU memory,
                        ArmReg[][] allRegisters,
                        Time time)
          throws ParseException {
    this.memory = memory;
    this.allRegisters = allRegisters;
    this.time = time;

    initEnvironment();
    
    List<String> lexemeList = lexemeDecomposition(sourceCode);
    if (!isWellStrutured(lexemeList)) {
      throw new ParseException("The expression is not well structured.");
    }
    Object nTree = transformIntoNTree(lexemeList);
    ScmExpr expr = constructExpr(nTree);

    if (expr instanceof BoolExpr) {
      return (BoolExpr) expr;
    } else {
      throw new ParseException("The type of the expression \"" + sourceCode + "\" must be a boolean.");
    }
  }

  /**
   * Decompose the string into a List of Strings called "lexemes".
   */
  private List<String> lexemeDecomposition(String source) {
    List<String> vLex = new ArrayList<String>();
    int begin = 0;
    while (begin < source.length()) {
      char c = source.charAt(begin);
      switch (c) {
        case '(':
        case ')':
          vLex.add("" + c);
        case ' ':
          begin++;
          break;
        default:
          int end = begin + 1;
          while ((end < source.length())
                  && (source.charAt(end) != ')')
                  && (source.charAt(end) != '(')
                  && (source.charAt(end) != ' ')) {
            end++;
          }
          vLex.add(source.substring(begin, end));
          begin = end;
          break;
      }
    }

    return vLex;
  }

  /**
   * Return true if and only if the list of String l
   * represent a well structured expression for this parser.
   */
  private boolean isWellStrutured(List<String> lexemeList) {
    if (lexemeList.isEmpty())
      return false;

    String elm = lexemeList.get(0);

    if (lexemeList.size() == 1)
      return (!elm.equals("(") && !elm.equals(")"));

    if (!elm.equals("("))
      return false;

    int parenthesisCount = 0;
    for (int i = 0; i < lexemeList.size(); i++) {
      String elm2 = (String) lexemeList.get(i);

      if (elm2.equals("(")) {
        parenthesisCount++;
        if (i >= lexemeList.size() - 1)
          return false;

        String elm3 = (String) lexemeList.get(i + 1);
        if (elm3.equals(")"))
          return false;
      }
      else if (elm2.equals(")"))
        parenthesisCount--;

      if (parenthesisCount == 0)
        if (i != lexemeList.size() - 1)
          return false;
    }

    if (parenthesisCount != 0)
      return false;

    return true;
  }

  /**
   * Create a nTree from the list of lexemes,
   * using the strings "(" and ")" as delimiters
   * of the nodes.
   */
  private Object transformIntoNTree(List<String> lexemeList) {
    /* The list l is supposed to be well structured
    and to have at least one element. */
    String elm = lexemeList.get(0);

    if (elm.equals("(")) {
      int position = 0;
      List l2 = new ArrayList();

      do {
        position++;
        String elm2 = lexemeList.get(position);

        if (elm2.equals(")")) {
          return l2;
        }

        int paramBeginPosition;
        int paramEndPosition;

        if (elm2.equals("(")) {
          /* The parameter is a list, so we have to look
          for its beginning and its end. */
          paramBeginPosition = position;
          int parenthesisCount = 1;
          do {
            position++;
            String elm3 = lexemeList.get(position);
            if (elm3.equals("(")) {
              parenthesisCount++;
            } else if (elm3.equals(")")) {
              parenthesisCount--;
            }
          } while (parenthesisCount != 0);
          paramEndPosition = position + 1;
        } else // It's a singleton, not a list.
        {
          paramBeginPosition = position;
          paramEndPosition = position + 1;
        }

        List<String> lParam = lexemeList.subList(paramBeginPosition, paramEndPosition);
        l2.add(transformIntoNTree(lParam));

      } while (true);
    } else {
      return elm;
    }
  }

  /**
   * Create a ScmExpr from a nTree composed of List and String objects.
   */
  protected ScmExpr constructExpr(Object nTree)
          throws ParseException {
    if (nTree instanceof List) {
      List l = (List) nTree;
      if (l.get(0) instanceof List) {
        throw new ParseException("A function is required.\nGiven : " + listToString(l.get(0)));
      }
      String f = ((String) l.get(0)).toLowerCase();
      ScmExpr result;
      int arity = -1;

      try {
        if (f.equals("or")) {
          arity = 2;
          ScmExpr arg1 = constructExpr(l.get(1));
          ScmExpr arg2 = constructExpr(l.get(2));

          if ((arg1 instanceof BoolExpr)
                  && (arg2 instanceof BoolExpr)) {
            result = new BoolOr((BoolExpr) arg1,
                    (BoolExpr) arg2);
          } else if ((arg1 instanceof IntExpr)
                  && (arg2 instanceof IntExpr)) {
            result = new IntOr((IntExpr) arg1,
                    (IntExpr) arg2);
          } else {
            throw new ClassCastException();
          }
        } else if (f.equals("and")) {
          arity = 2;
          ScmExpr arg1 = constructExpr(l.get(1));
          ScmExpr arg2 = constructExpr(l.get(2));

          if ((arg1 instanceof BoolExpr)
                  && (arg2 instanceof BoolExpr)) {
            result = new BoolAnd((BoolExpr) arg1,
                    (BoolExpr) arg2);
          } else if ((arg1 instanceof IntExpr)
                  && (arg2 instanceof IntExpr)) {
            result = new IntAnd((IntExpr) arg1,
                    (IntExpr) arg2);
          } else {
            throw new ClassCastException();
          }
        } else if (f.equals("=")) {
          arity = 2;
          ScmExpr arg1 = constructExpr(l.get(1));
          ScmExpr arg2 = constructExpr(l.get(2));

          if ((arg1 instanceof BoolExpr)
                  && (arg2 instanceof BoolExpr)) {
            result = new BoolEqualBoolBool((BoolExpr) arg1,
                    (BoolExpr) arg2);
          } else if ((arg1 instanceof IntExpr)
                  && (arg2 instanceof IntExpr)) {
            result = new BoolEqualIntInt((IntExpr) arg1,
                    (IntExpr) arg2);
          } else {
            throw new ClassCastException();
          }
        } else if (f.equals(">")) {
          arity = 2;
          result = new BoolUGreaterThan((IntExpr) constructExpr(l.get(1)),
                  (IntExpr) constructExpr(l.get(2)));
        } else if (f.equals("<")) {
          arity = 2;
          result = new BoolUGreaterThan((IntExpr) constructExpr(l.get(2)),
                  (IntExpr) constructExpr(l.get(1)));
        } else if (f.equals("s>")) {
          arity = 2;
          result = new BoolSGreaterThan((IntExpr) constructExpr(l.get(1)),
                  (IntExpr) constructExpr(l.get(2)));
        } else if (f.equals("s<")) {
          arity = 2;
          result = new BoolSGreaterThan((IntExpr) constructExpr(l.get(2)),
                  (IntExpr) constructExpr(l.get(1)));
        } else if (f.equals("-")) {
          arity = 2;
          result = new IntMinus((IntExpr) constructExpr(l.get(1)),
                  (IntExpr) constructExpr(l.get(2)));
        } else if (f.equals("+")) {
          arity = 2;
          result = new IntPlus((IntExpr) constructExpr(l.get(1)),
                  (IntExpr) constructExpr(l.get(2)));
        } else if (f.equals("xor")) {
          arity = 2;
          result = new IntXor((IntExpr) constructExpr(l.get(1)),
                  (IntExpr) constructExpr(l.get(2)));
        } else if (f.equals("not")) {
          arity = 1;
          ScmExpr arg = constructExpr(l.get(1));

          if (arg instanceof BoolExpr) {
            result = new BoolNot((BoolExpr) arg);
          } else {
            result = new IntNot((IntExpr) arg);
          }
        } else if (f.equals("neg")) {
          arity = 1;
          result = new IntNeg((IntExpr) constructExpr(l.get(1)));
        } else if (f.equals("mem32")) {
          arity = 1;
          result = new IntMem32((IntExpr) constructExpr(l.get(1)), memory);
        } else if (f.equals("smem16")) {
          arity = 1;
          result = new IntSMem16((IntExpr) constructExpr(l.get(1)), memory);
        } else if (f.equals("umem16")) {
          arity = 1;
          result = new IntUMem16((IntExpr) constructExpr(l.get(1)), memory);
        } else if (f.equals("smem8")) {
          arity = 1;
          result = new IntSMem8((IntExpr) constructExpr(l.get(1)), memory);
        } else if (f.equals("umem8")) {
          arity = 1;
          result = new IntUMem8((IntExpr) constructExpr(l.get(1)), memory);
        } else if (f.equals("mem8read")) {
          arity = 1;
          IntExpr arg = (IntExpr) constructExpr(l.get(1));
          result = new BoolMem8Read(arg, memory);
        } else if (f.equals("mem16read")) {
          arity = 1;
          IntExpr arg = (IntExpr) constructExpr(l.get(1));
          result = new BoolMem16Read(arg, memory);
        } else if (f.equals("mem32read")) {
          arity = 1;
          IntExpr arg = (IntExpr) constructExpr(l.get(1));
          result = new BoolMem32Read(arg, memory);
        } else if (f.equals("mem8written")) {
          arity = 1;
          IntExpr arg = (IntExpr) constructExpr(l.get(1));
          result = new BoolMem8Written(arg, memory);
        } else if (f.equals("mem16written")) {
          arity = 1;
          IntExpr arg = (IntExpr) constructExpr(l.get(1));
          result = new BoolMem16Written(arg, memory);
        } else if (f.equals("mem32written")) {
          arity = 1;
          IntExpr arg = (IntExpr) constructExpr(l.get(1));
          result = new BoolMem32Written(arg, memory);
        } else if (f.equals("reg")) {
          arity = 1; // The one-argument version of the function.
          String regName = (String) l.get(1);
          String modeName = null;
          if (l.size() == 3) {
            arity = 2; // finally, it uses the 2-args version of the function.
            modeName = ((String) l.get(2)).toLowerCase();
          }
          ArmReg reg = getRegister(regName.toLowerCase(), modeName);
          result = new IntReg(reg);
        } else if (f.equals("regread")) {
          arity = 1; // The one-argument version of the function.
          String regName = (String) l.get(1);
          String modeName = null;
          if (l.size() == 3) {
            arity = 2; // finally, it uses the 2-args version of the function.
            modeName = ((String) l.get(2)).toLowerCase();
          }
          ArmReg reg = getRegister(regName.toLowerCase(), modeName);
          result = new BoolRegRead((ArmRegObserver) reg);
        } else if (f.equals("regwritten")) {
          arity = 1; // The one-argument version of the function.
          String regName = (String) l.get(1);
          String modeName = null;
          if (l.size() == 3) {
            arity = 2; // finally, it uses the 2-args version of the function.
            modeName = ((String) l.get(2)).toLowerCase();
          }
          ArmReg reg = getRegister(regName.toLowerCase(), modeName);
          result = new BoolRegWritten((ArmRegObserver) reg);
        } else if (f.equals("time")) {
          arity = 0;
          result = new IntTime(time);
        } /*
        else if (f.equals("display"))
        {
        arity = 1;
        IntExpr expr = (IntExpr) constructExpr(l.get(1));
        result = new BoolDisplayInt(listToString(l.get(1)), expr);
        }

        else if (f.equals("log"))
        {
        arity = 1;
        IntExpr expr = (IntExpr) constructExpr(l.get(1));
        result = new BoolLogInt(listToString(l.get(1)), expr);
        }
         */ else {
          throw new ParseException("Unknown function: " + f);
        }

        if (arity != l.size() - 1) {
          throw new IndexOutOfBoundsException();
        }
      } catch (IndexOutOfBoundsException e) {
        String msg = "procedure " + f + ": expects " + arity + " argument";
        if (arity > 1) {
          msg += "s";
        }
        msg += ", given " + (l.size() - 1);
        if (l.size() > 1) {
          msg += ":";
        }
        for (int i = 1; i < l.size(); i++) {
          msg += " " + listToString(l.get(i));
        }
        throw new ParseException(msg);
      } catch (ClassCastException e) {
        String msg = "procedure " + f + ": Bad type of argument";
        if (arity > 1) {
          msg += "s";
        }
        msg += " in expression " + listToString(l);
        throw new ParseException(msg);
      }

      return result;
    } else // Here is the evaluation of symbols.
    {
      String symbol = (String) nTree;
      ScmExpr expr = getScmExpr(symbol);
      if (expr != null) {
        return expr;
      } else {
        try {
          return new IntConst(Long.decode(symbol).intValue());
        } catch (NumberFormatException e) {
          throw new ParseException("Undefined symbol: " + symbol);
        }
      }
    }
  }

  private boolean canBeDecodedAsANumber(String symbol) {
    try {
      Long.decode(symbol);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private String listToString(Object nTree) {
    if (nTree instanceof List) {
      List l = (List) nTree;
      String result = "(";
      for (int i = 0; i < l.size(); i++) {
        result += listToString(l.get(i));
        if (i < l.size() - 1) {
          result += " ";
        }
      }
      result += ")";
      return result;
    } else {
      return (String) nTree;
    }
  }

  private ArmReg getRegister(String regName, String modeName)
          throws ParseException {
    if (allRegisters == null)
      return null;

    int modeNumber = Arm7Tdmi.svcModeBits;

    if (modeName != null) {
      if (modeName.equals("usr")) {
        modeNumber = Arm7Tdmi.usrModeBits;
      } else if (modeName.equals("fiq")) {
        modeNumber = Arm7Tdmi.fiqModeBits;
      } else if (modeName.equals("irq")) {
        modeNumber = Arm7Tdmi.irqModeBits;
      } else if (modeName.equals("svc")) {
        modeNumber = Arm7Tdmi.svcModeBits;
      } else if (modeName.equals("abt")) {
        modeNumber = Arm7Tdmi.abtModeBits;
      } else if (modeName.equals("und")) {
        modeNumber = Arm7Tdmi.undModeBits;
      } else if (modeName.equals("sys")) {
        modeNumber = Arm7Tdmi.sysModeBits;
      } else {
        throw new ParseException("Unknown cpu-mode name");
      }
    }

    int regNumber;

    if (regName.equals("r0")) {
      regNumber = 0;
    } else if (regName.equals("r1")) {
      regNumber = 1;
    } else if (regName.equals("r2")) {
      regNumber = 2;
    } else if (regName.equals("r3")) {
      regNumber = 3;
    } else if (regName.equals("r4")) {
      regNumber = 4;
    } else if (regName.equals("r5")) {
      regNumber = 5;
    } else if (regName.equals("r6")) {
      regNumber = 6;
    } else if (regName.equals("r7")) {
      regNumber = 7;
    } else if (regName.equals("r8")) {
      regNumber = 8;
    } else if (regName.equals("r9")) {
      regNumber = 9;
    } else if (regName.equals("r10")) {
      regNumber = 10;
    } else if (regName.equals("r11")) {
      regNumber = 11;
    } else if (regName.equals("r12")) {
      regNumber = 12;
    } else if (regName.equals("r13") || regName.equals("sp")) {
      regNumber = 13;
    } else if (regName.equals("r14") || regName.equals("lr")) {
      regNumber = 14;
    } else if (regName.equals("r15") || regName.equals("pc")) {
      regNumber = 15;
    } else if (regName.equals("cpsr")) {
      regNumber = 16;
    } else if (regName.equals("spsr")) {
      regNumber = 17;
    } else {
      throw new ParseException("Unknown register name");
    }

    return allRegisters[modeNumber][regNumber];
  }

  private void define(String symbol, ScmExpr value) {
    environment.put(symbol, value);
  }

  private ScmExpr getScmExpr(String symbol) {
    return environment.get(symbol);
  }

}
