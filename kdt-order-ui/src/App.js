import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { CustomerList } from './components/CustomerList';
import { Customer } from './components/Customer';

function App() {
  return (
    <Container>
      <Router>
        <Routes>
          <Route path='/customers/:customerId' element={ <Customer /> } />
          <Route path='/customers' element={ <CustomerList /> } />
          <Route path='/' element={ <Navigate to="/customers" /> } />
        </Routes>
      </Router>
    </Container>
  );
}

export default App;
