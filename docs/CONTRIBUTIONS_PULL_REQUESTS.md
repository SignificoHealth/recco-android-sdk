# PROJECT CONTRIBUTION VIA PULL REQUESTS

This document outlines the guidelines and best practices for contributing through pull requests (PRs).

## Branching Strategy

The `main` branch is protected with a set of rules and only via PRs changes can be merged to it. When working on a contribution, create a new branch with a descriptive name related to the issue or feature you are addressing. As part of the name, set a reference to the Jira ticket. 

## Commit Messages

Write clear and concise commit messages that explain the purpose of each commit.

## Documentation

Update relevant documentation when making changes. This includes README files, code comments, and user-facing documentation.

## Review

Before submitting a PR, review your changes thoroughly to catch errors and ensure the code is functioning as expected.

## Pull Request Process

1. When you are ready to submit your changes, open a pull request from your working branch targeting main. Reference any relevant issues in the PR description.
2. To be able to merge the PR, the branch needs to be up to date with `main` and the PR reviewed and approved by other team member, along with succeeding the [STATUS_CHECKS][STATUS-CHECKS] that are executed as part of the `pull-request` Github action.

[STATUS-CHECKS]:./docs/STATUS_CHECKS.md
