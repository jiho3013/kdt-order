import { useLayoutEffect, useState } from 'react';
import axios from 'axios';
import { Table } from 'react-bootstrap';
import { Link } from 'react-router-dom';

export function CustomerList() {
  const [customers, setCustomers] = useState([]);

  useLayoutEffect(() => {
    axios.get('http://localhost:8080/kdt/api/v1/customers', {
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then(v => {
        setCustomers(v.data);
      });
  }, []);

  return (
    <>
      <h1>Customer Table</h1>
      <Table striped bordered hover>
        <thead>
        <tr>
          <th>id</th>
          <th>name</th>
          <th>email</th>
          <th>createdAt</th>
          <th>lastLoginAt</th>
        </tr>
        </thead>
        <tbody>
        {customers.map(v =>
          <tr key={v.customerId}>
            <td><Link to={`/customers/${v.customerId}`}>{v.customerId}</Link></td>
            <td>{v.name}</td>
            <td>{v.email}</td>
            <td>{v.createdAt}</td>
            <td>{v.lastLoginAt}</td>
          </tr>,
        )}
        </tbody>
      </Table>
    </>);
}