Feature: Data table feature

  Scenario: Scenario with a data table
    Given given2
      | column1  | column2  |
      | value1_1 | value1_2 |
      | value2_1 | value2_2 |
    When when2
    Then then2
