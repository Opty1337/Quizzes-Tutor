const QUIZ_TITLE = 'My Quiz Test';
const CLARIFICATION_CONTENT = 'THIS IS A CLARIFICATION REQUEST TEST';
const QUESTION = 'Utility tree';

describe('Clarification Request walkthrough', () => {
  before(() => {
    cy.demoTeacherLogin();
    cy.createQuiz(QUIZ_TITLE, QUESTION);
    cy.demoStudentLogin();
    cy.get('.quizzesButton').click();
    cy.respondQuiz(QUIZ_TITLE);
  });

  beforeEach(() => {
    cy.demoStudentLogin();
    cy.get('.quizzesButton').click();
  });

  afterEach(() => {
    cy.contains('Logout').click();
  });

  it('Create a invalid clarification request', () => {
    cy.goToClarification(QUIZ_TITLE);
    cy.createInvalidClarificationRequest();
  });

  it('Creates a clarification request', () => {
    cy.goToClarification(QUIZ_TITLE);
    cy.createClarificationRequest(CLARIFICATION_CONTENT);
  });

  it('Show Question of a clarification request submitted', () => {
    cy.showClarifications();
    cy.showQuestionClarification(CLARIFICATION_CONTENT);
  });

  it('Show Clarification of a Clarification request submitted', () => {
    cy.showClarifications();
    cy.showClarification(CLARIFICATION_CONTENT);
  });
});
