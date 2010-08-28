package com.lemoulinstudio.gfa.core.cpu.test;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author vincent
 */
public class InstructionDecoderTest {

  public void testArmInstructions() {
    List<Instruction> armInstructions = Arrays.<Instruction>asList(
            new Instruction("BX",     0x0ffffff0, 0x012fff10),
            new Instruction("B",      0x0e000000, 0x0a000000),
            new Instruction("MRS",    0x0fbf0fff, 0x010f0000),
            new Instruction("MSR1",   0x0fbffff0, 0x0129f000),
            new Instruction("MSR2",   0x0dbff000, 0x0128f000),
            new Instruction("MUL",    0x0fc000f0, 0x00000090),
            new Instruction("MULL",   0x0f8000f0, 0x00800090),
            new Instruction("LDRSTR", 0x0c000000, 0x04000000),
            new Instruction("HSDTRO", 0x0e400f90, 0x00000090),
            new Instruction("HSDTIO", 0x0e400090, 0x00400090),
            new Instruction("LDMSTM", 0x0e000000, 0x08000000),
            new Instruction("SWP",    0x0fb00ff0, 0x01000090),
            new Instruction("DataProcessing", 0x0c000000, 0x00000000),
            new Instruction("DataProcessingAND", 0x01e00000, 0x00000000),
            new Instruction("DataProcessingEOR", 0x01e00000, 0x00200000),
            new Instruction("DataProcessingSUB", 0x01e00000, 0x00400000),
            new Instruction("DataProcessingRSB", 0x01e00000, 0x00600000),
            new Instruction("DataProcessingADD", 0x01e00000, 0x00800000),
            new Instruction("DataProcessingADC", 0x01e00000, 0x00a00000),
            new Instruction("DataProcessingSBC", 0x01e00000, 0x00c00000),
            new Instruction("DataProcessingRSC", 0x01e00000, 0x00e00000),
            new Instruction("DataProcessingTST", 0x01e00000, 0x01000000),
            new Instruction("DataProcessingTEQ", 0x01e00000, 0x01200000),
            new Instruction("DataProcessingCMP", 0x01e00000, 0x01400000),
            new Instruction("DataProcessingCMN", 0x01e00000, 0x01600000),
            new Instruction("DataProcessingORR", 0x01e00000, 0x01800000),
            new Instruction("DataProcessingMOV", 0x01e00000, 0x01a00000),
            new Instruction("DataProcessingBIC", 0x01e00000, 0x01c00000),
            new Instruction("DataProcessingMVN", 0x01e00000, 0x01e00000),
            new Instruction("SWI",    0x0f000000, 0x0f000000),
            new Instruction("Undef",  0x0e000010, 0x06000010)
            );

    for (Instruction a : armInstructions)
      for (Instruction b : armInstructions)
        if (a != b)
          if (a.isPrefixOf(b))
            System.out.printf("%s is prefix of %s\n", a.getName(), b.getName());
  }

  public static void main(String[] args) {
    InstructionDecoderTest instructionDecoderTest = new InstructionDecoderTest();
    instructionDecoderTest.testArmInstructions();
  }

}
