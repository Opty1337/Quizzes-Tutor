// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })
/// <reference types="Cypress" />

Cypress.Commands.add('demoStudentLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="studentButton"]').click();
});

Cypress.Commands.add('demoTeacherLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="teacherButton"]').click();
});

Cypress.Commands.add('demoAdminLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="adminButton"]').click();
  cy.contains('Administration').click();
  cy.contains('Manage Courses').click();
});

Cypress.Commands.add('demoStudentLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="BttnStudentLogin"]').click();
});

Cypress.Commands.add('demoTeacherLogin', () => {
  cy.visit('/');
  cy.get('[data-cy="BttnTeacherLogin"]').click();
  cy.get('.bttnManagement').click();
});

Cypress.Commands.add('createCourseExecution', (name, acronym, academicTerm) => {
  cy.get('[data-cy="createButton"]').click();
  cy.get('[data-cy="Name"]').type(name);
  cy.get('[data-cy="Acronym"]').type(acronym);
  cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
  cy.get('[data-cy="saveButton"]').click();
});

Cypress.Commands.add('closeErrorMessage', (name, acronym, academicTerm) => {
  cy.contains('Error')
    .parent()
    .find('button')
    .click();
});

Cypress.Commands.add(
  'createStudentSuggestion',
  ({ questionTitle, questionContent, options }) => {
    cy.contains('Suggested').click();
    cy.get('[data-cy="newQuestionBtn"]').click();
    questionTitle && cy.get('.questionTitle').type(questionTitle);
    cy.get('[data-cy="questionContent"]').type(questionContent);
    cy.get('[data-cy="correct1"]').click({ force: true });
    cy.get('[data-cy="option1"]').type(options[0]);
    cy.get('[data-cy="option2"]').type(options[1]);
    cy.get('[data-cy="option3"]').type(options[2]);
    cy.get('[data-cy="option4"]').type(options[3]);
    cy.get('[data-cy="saveQuestionBtn"]').click();
    cy.wait(100);
  }
);

Cypress.Commands.add(
  'createEvaluation',
  ({ questionTitle, questionContent, options }, justification) => {
    cy.contains('Management').click();
    cy.contains('Student Questions').click();
    cy.contains('Title').click();
    cy.contains(questionContent)
      .parent()
      .parent()
      .children()
      .find('[data-cy="evaluationBtn"]')
      .click();
    cy.get('[data-cy="accepted"]').click({ force: true });
    cy.get('[data-cy="justification"]').type(justification);
    cy.contains('Save').click();
    cy.contains(questionContent)
      .parent()
      .parent()
      .children()
      .find('[data-cy="evaluationBtn"]')
      .click();
    cy.contains('close').click();
  }
);

Cypress.Commands.add('deleteCourseExecution', acronym => {
  cy.contains(acronym)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 7)
    .find('[data-cy="deleteCourse"]')
    .click();
});

Cypress.Commands.add(
  'createFromCourseExecution',
  (name, acronym, academicTerm) => {
    cy.contains(name)
      .parent()
      .should('have.length', 1)
      .children()
      .should('have.length', 7)
      .find('[data-cy="createFromCourse"]')
      .click();
    cy.get('[data-cy="Acronym"]').type(acronym);
    cy.get('[data-cy="AcademicTerm"]').type(academicTerm);
    cy.get('[data-cy="saveButton"]').click();
  }
);

Cypress.Commands.add('createQuiz', (title, question) => {
  cy.get('.bttnManagement').click();
  cy.get('[href="/management/quizzes"]').click();
  cy.contains('Title').click();
  cy.get('[data-cy="bttnCreateQuiz"]').click();
  cy.get('[data-cy="QuizTitle"]').type(title);
  cy.get('[data-cy="bttnSearch"]').type(question);
  cy.contains(question)
    .parent()
    .should('have.length', 1)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 7)
    .find('[data-cy="ActAddQuestion"]')
    .click();
  cy.get('[data-cy="availableDate"]').click();
  cy.get('.v-date-picker-header')
    .children()
    .first()
    .click();
  cy.wait(500);
  cy.get('.v-date-picker-table')
    .children()
    .contains('28')
    .click();
  cy.get('.green--text').click();
  cy.get('[data-cy="type"]').click();
  cy.contains('PROPOSED').click();
  cy.contains('Save').click();
  cy.wait(100);
});

Cypress.Commands.add('respondQuiz', quiz_title => {
  cy.contains('Available').click();
  cy.contains(quiz_title).click();
  cy.get('.end-quiz').click();
  cy.get('[data-cy="imSure"]').click();
  cy.get('.quizzesButton').click();
});

Cypress.Commands.add('goToClarification', quiz_title => {
  cy.contains('Solved').click();
  cy.contains(quiz_title).click();
  cy.contains('Show Clarifications').click();
});

Cypress.Commands.add('createInvalidClarificationRequest', () => {
  cy.contains('Create').click();
  cy.get('[data-cy="bttnClrfSave"]').click();
  cy.contains('Clarification must have Content');
});

Cypress.Commands.add('createClarificationRequest', clrfContent => {
  cy.contains('Create').click();
  cy.get('[data-cy="ClrfReq"]').type(clrfContent);
  cy.get('[data-cy="bttnClrfSave"]').click();
  cy.contains(clrfContent).click();
});

Cypress.Commands.add('createInvalidClarificationComment', crlfContent => {
  cy.contains(crlfContent)
    .parent()
    .children()
    .should('have.length', 6)
    .find('[data-cy="showClrf"]')
    .click();
  cy.get('[data-cy="bttnAddComment"]').click();
  cy.get('[data-cy="bttnCmtSave"]').click();
  cy.contains('Comment must have Content');
});

Cypress.Commands.add(
  'createClarificationCommentAct',
  (clrfContent, cmtContent) => {
    cy.contains(clrfContent)
      .parent()
      .children()
      .should('have.length', 6)
      .find('[data-cy="addCmt"]')
      .click();
    cy.get('[data-cy="cmtContent"]').type(cmtContent);
    cy.get('[data-cy="bttnCmtSave"]').click();
    cy.contains(clrfContent)
      .parent()
      .children()
      .should('have.length', 6)
      .contains('RESOLVED')
      .click();
  }
);

Cypress.Commands.add(
  'createClarificationCommentChat',
  (clrfContent, cmtContent) => {
    cy.contains(clrfContent)
      .parent()
      .children()
      .should('have.length', 6)
      .find('[data-cy="showClrf"]')
      .click();
    cy.get('[data-cy="bttnAddComment"]').click();
    cy.get('[data-cy="cmtContent"]').type(cmtContent);
    cy.get('[data-cy="bttnCmtSave"]').click();
    cy.contains(cmtContent);
  }
);

Cypress.Commands.add('showClarifications', () => {
  cy.contains('Clarifications').click();
});

Cypress.Commands.add('showQuestionClarification', clrfContent => {
  cy.contains(clrfContent)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 6)
    .find('[data-cy="showQuestion"]')
    .click();
  cy.get('[data-cy="bttnClose"]').click();
});

Cypress.Commands.add('showClarification', clrfContent => {
  cy.contains(clrfContent)
    .parent()
    .children()
    .should('have.length', 6)
    .find('[data-cy="showClrf"]')
    .click();
  cy.get('[data-cy="bttnClose"]').click();
});

Cypress.Commands.add('clickRowButton', (elem, button) => {
  cy.contains(elem)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 8)
    .find('[data-cy="' + button + '"]')
    .click();
});

Cypress.Commands.add('assertRowField', (elem, field, value) => {
  cy.contains(elem)
    .parent()
    .should('have.length', 1)
    .children()
    .should('have.length', 8)
    .find('[data-cy="' + field + '"]')
    .first()
    .should('have.text', value);
});

Cypress.Commands.add('createTournament', (name, day, nrQuestions) => {
  cy.get('[data-cy="newTdPButton"]').click();
  cy.get('[data-cy="newTdPName"]').type(name);
  cy.get('[data-cy="newTdPStartDateMenu"]').click();
  cy.wait(500);
  cy.get('.v-date-picker-header')
    .children()
    .last()
    .click();
  cy.wait(500);
  cy.get('.v-date-picker-table')
    .children()
    .contains(day.toString())
    .click();
  cy.wait(500);
  cy.get('[data-cy="newTdPNrQuestions"]')
    .clear()
    .type(nrQuestions.toString());
  cy.get('[data-cy="newTdPTopicsMenu"]').click();
  cy.wait(500);
  cy.get('[data-cy="newTdPTopic"]')
    .first()
    .click();
  cy.get('[data-cy="newTdPTopic"]')
    .last()
    .click();
  cy.get('[data-cy="newTdPSave"]').click();
  cy.wait(500);
});
