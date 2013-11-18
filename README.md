kbilling-plans
==============
This repo contains example billing plans for use with [KillingBilling](https://www.killingbilling.com/) - Agile Cloud Billing. 

# Getting Started

### Create a new project

1.  Go to the [KillingBilling dashboard](/dashboard).
2.  Create a project.

    a.  Let's assume, your new project is linked to a new repo forked from [KillingBilling/kbilling-plans](https://github.com/KillingBilling/kbilling-plans).

    b.  An **API endpoint** is created and your KillingBilling API project is deployed to this endpoint.

### Write your billing plans

1.  Tweak `com.example.ExamplePlan` and `com.example.ExamplePlan2` or write your own.
2.  Tweak `src/main/resources/plans.json` file.
3.  Commit changes, push to GitHub.
4.  Deploy your KillingBilling project to the API Endpoint: hit **Deploy** on the project page.

### Start using KillingBilling API

You can interact with the API using `curl` utility.

Get **API Endpoint** and **API Key** from your KillingBilling project page and:

*   add them to your `~/.netrc` file (to authenticate with `curl -n`):

        machine {API_endpoint_host}
          password {API_key}

*   export your API Endpoint URI (without the trailing `/`) in `API_endpoint` env var, like this:

        export API_endpoint='https://quiet-woodland-9149.herokuapp.com'


You'll need `uuidgen` utility to generate UUIDs for every new transaction (so that it can be retried, in case of trouble). This is _not_ required, but highly recommended.

### OK, let's go!

1.  Show the available **billing plans**:

        $ curl -i -n -X GET ${API_endpoint}/plans

        ## output:
        [
           {"key":"examplePlan", "impl":"com.example.ExamplePlan"},
           {"key":"examplePlan2", "impl":"com.example.ExamplePlan2"}
        ]

2.  Subscribe a **client** to a **billing plan**:

        $ curl -i -n -X POST ${API_endpoint}/clients/cl-111/subscription \
          -d '{"txn":"7241ad1b-d619-43e5-9150-94081a0bcd2a","plan":"examplePlan2"}'

3.  Initiate a **billing cycle** for a **client**:

        $ curl -i -n -X POST ${API_endpoint}/clients/cl-111/bcycle/init \
          -d '{"txn":"e8c2a376-239c-4c85-9653-1a404a3a4e68"}'

4.  Get **client** info:

        $ curl -i -n -X GET {API_endpoint}/clients/cl-111

        ## output:
        {
          "vars":{},
          "subscription":{
            "plan":"examplePlan2",
            "txn":"7241ad1b-d619-43e5-9150-94081a0bcd2a",
            "recordTime":"2013-06-12T12:53:21.389Z"
          },
          "bcycle":{
            "txn":"e8c2a376-239c-4c85-9653-1a404a3a4e68",
            "recordTime":"2013-06-12T13:16:59.635Z"
          }
        }

5.  Submit **transactions** with account **operations**:

    a.  Grant some free service resource amount to a **client**

        $ curl -i -n -X POST ${API_endpoint}/txns \
          -d '{"txn": "0a9ab193-3524-4cf7-9c1e-63b7962d3622","ops":{"cl-111":{"Bones":3.00}}}'

        #client cl-111 info:
        $ curl -i -n -X GET ${API_endpoint}/clients/cl-111

        ## output:
        {
          "vars":{
            "Bones":3.00,
            "USD_cost":0.0,
            "USD":0.0
          }/* skip */
        }

    b.  **Client** uses some amount of service resource

        $ curl -i -n -X POST ${API_endpoint}/txns \
          -d '{"txn":"a76e9016-daf2-4597-80b5-3f6fd2ac42a7",
               "ops":{"cl-111":{"Bones":-5.00}}}'

        #client cl-111 info:
        $ curl -n -X GET ${API_endpoint}/clients/cl-111

        ## output:
        {
          "vars":{
            "Bones":0,
            "BonesSUM":2.00,
            "USD_cost":0.200,
            "USD":-0.200
          }/* skip */
        }

    c.  Transfer funds from **client** `cl-111` to **client** `cl-222`

        $ curl -i -n -X POST ${API_endpoint}/txns \
          -d '{"txn":"ef3625b6-e705-46b4-a0ea-ca3bd47473bd",
               "ops":{"cl-111":{"USD":10.0},"cl-222":{"USD":-10.0}}}'

        #client cl-111 info:
        $ curl -n -X GET ${API_endpoint}/clients/cl-111

        ## output:
        {
          "vars":{
            "Bones":0,
            "BonesSUM":2.00,
            "USD_cost":0.200,
            "USD":9.800
          }/* skip */
        }

        #client cl-222 info:
        $ curl -n -X GET ${API_endpoint}/clients/cl-222

        ## output:
        {
          "vars":{
            "USD":-10.0
          }/* skip */
        }

6.  Get **client** info for some point in the past:

        $ curl -n -X GET ${API_endpoint}/clients/cl-111/at/2013-06-13T09:30:00.000Z

        ## output:
        {
          "vars":{
            "Bones":0,
            "BonesSUM":2.00,
            "USD_cost":0.200,
            "USD":-0.200
          }/* skip */
        }
