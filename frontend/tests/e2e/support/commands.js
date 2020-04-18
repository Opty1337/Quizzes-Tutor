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
  }
);

Cypress.Commands.add(
  'createEvaluation',
  ({ questionTitle, questionContent, options }, justification) => {
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
