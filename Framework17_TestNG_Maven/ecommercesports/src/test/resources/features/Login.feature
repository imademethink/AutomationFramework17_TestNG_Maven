Feature: Demo of Maven and Junit integration - Login

@login_valid @login
Scenario: Login happy path case
  When User attempts to login with Username "creativity2020@mailinator.com" and Password "creativity2020@@"
  Then Login should be successful


@login_invalid @login
Scenario: Login un-happy path case
  When User attempts to login stackoverflow


@dummy
Scenario: dummy test
  When dummy step