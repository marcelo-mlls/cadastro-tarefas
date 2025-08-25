describe('Navegação Principal', () => {
  it('deve navegar para a página de usuários e verificar o título', () => {
    // Visita a URL base da aplicação
    // Nota: A aplicação deve estar rodando (docker-compose up)
    cy.visit('http://localhost:4200/');

    // Encontra o link que contém "Gerenciar Usuários" e clica nele
    cy.contains('Gerenciar Usuários').click();

    // Verifica se a URL mudou para /users
    cy.url().should('include', '/users');

    // Verifica se a página contém um h2 com o texto "Lista de Usuários"
    cy.get('h2').contains('Lista de Usuários').should('be.visible');
  });
});