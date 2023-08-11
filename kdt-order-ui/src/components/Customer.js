import { useLayoutEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Button, Form } from 'react-bootstrap';

export function Customer() {
  const [customer, setCustomer] = useState({});
  let { customerId } = useParams();
  useLayoutEffect(() => {
    axios.get('http://localhost:8080/kdt/api/v1/customers/' + customerId)
      .then(v => {
        console.log(v);
        setCustomer(v.data);
      });
  }, []);

  const submit = data => {
    axios.post('http://localhost:8080/kdt/api/v1/customers/' + customerId, data)
      .then(v => {
        setCustomer(v.data);
      });
  };

  return (
    <Form>
      <h1>Customer Details</h1>
      <Form.Group className='mb-3' controlId='formBasicEmail'>
        <Form.Label>Customer ID</Form.Label>
        <Form.Control type='email' value={customer.customerId} readOnly={true} />
      </Form.Group>

      <Form.Group className='mb-3' controlId='formBasicEmail'>
        <Form.Label>Email address</Form.Label>
        <Form.Control type='email' value={customer.email} />
      </Form.Group>

      <Form.Group className='mb-3' controlId='formBasicPassword'>
        <Form.Label>Name</Form.Label>
        <Form.Control type='text' value={customer.name} readOnly={true} />
      </Form.Group>

      <Button onClick={(e) => submit(customer)}>Submit</Button>
    </Form>
  );
}